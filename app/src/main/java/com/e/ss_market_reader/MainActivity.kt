package com.e.ss_market_reader

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URLDecoder

class MainActivity : AppCompatActivity() {

    private var products:ArrayList<Product> = ArrayList<Product>()
    private var userID : String = "";
    private lateinit var adapter : ProductAdapter
    private lateinit var product_lv : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        product_lv = findViewById<ListView>(R.id.product_lv)
        val pay_btn = findViewById<Button>(R.id.pay_btn)
        val back_btn = findViewById<ImageButton>(R.id.back_btn)

        adapter = ProductAdapter(products,this)

        product_lv.adapter = adapter

        pay_btn.setOnClickListener {pay_action()}
        back_btn.setOnClickListener {initQRcodeScanner()}

        initQRcodeScanner()
    }

    private fun initQRcodeScanner(){
        val integrator = IntentIntegrator(this)
        integrator.setBeepEnabled(false) //스캔시 소리 여부
        integrator.setOrientationLocked(true) //가로, 세로 고정
        integrator.setPrompt("QR코드를 인식하세요.")
        integrator.initiateScan()
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result : IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        /*if(result !=null) { //always true
            if(result.contents == null) {
                Toast.makeText(this,"인식 실패",Toast.LENGTH_SHORT).show()
                finish()
            } else {
                MY_action(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }*/
        if(result.contents == null) {
            Toast.makeText(this,"인식 실패",Toast.LENGTH_SHORT).show()
        } else {
            MY_action(result.contents)
            //toList(decodeContents(result.contents))
        }
    }
    private fun decodeContents(contents : String) : String{
        var json : String = ""
        try {
            json = URLDecoder.decode(contents,"UTF-8")
        }catch (e:Exception){
            e.printStackTrace()
        }
        return json
    }
    private fun toList(json : String){
        products.clear()
        val jsonObject : JSONObject = JSONObject(json)
        userID = jsonObject.getString("userID")
        val jsonArray : JSONArray = jsonObject.getJSONArray("list")
        val gson : Gson = GsonBuilder().create()
        try {
            for (i in 0 until jsonArray.length()){
                val product:Product = gson.fromJson(jsonArray.get(i).toString(),Product::class.java)
                products.add(product)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun total_cal(list : List<Product>){
        var sum = 0
        if(list.isNotEmpty()){
            for(p:Product in list){
                sum += p.price * p.amount
            }
        }
        val totalPrice = findViewById<TextView>(R.id.totalPrice)
        val id_tv = findViewById<TextView>(R.id.userID_tv)

        totalPrice.text = "총액 : $sum 원"
        id_tv.text = userID
    }


    private fun MY_action(contents: String){
        try {
            toList(decodeContents(contents))
        }catch (e : Exception){
            initQRcodeScanner()
        }

        total_cal(products)

        adapter.notifyDataSetChanged()
    }

    private fun pay_action(){
        var dialog : AlertDialog;
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        dialog = builder.setMessage("결제 완료")
            .setPositiveButton("확인") { _, _ ->
                //구매 완료 action
                Add_purchase()
            }
            .create()
        dialog.show()
    }

    private fun Add_purchase(){
        var is_success = true
        val responseListener : Response.Listener<String> = Response.Listener { response ->
            try {
                val jsonResponse = JSONObject(response)
                val success = jsonResponse.getBoolean("success")
                if (!success) {
                    Toast.makeText(this, "네트워크 에러", Toast.LENGTH_SHORT).show()
                    is_success = false
                }
            }catch (e : Exception){
                e.printStackTrace()
                is_success = false
            }
        }
        val queue : RequestQueue = Volley.newRequestQueue(this)
        for (p : Product in products) {
            p.price = p.price * p.amount;
            val request: PurchaseRequest = PurchaseRequest(userID, p,responseListener)
            queue.add(request)
        }
        if(is_success){
            products.clear()
            adapter.notifyDataSetChanged()
            userID = "none"
            total_cal(products)
            initQRcodeScanner()
        }
    }

}