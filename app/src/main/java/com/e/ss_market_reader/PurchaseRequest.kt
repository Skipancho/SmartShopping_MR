package com.e.ss_market_reader

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class PurchaseRequest : StringRequest{
    val target : String = "https://ctg1770.cafe24.com/SC/S_C_AddPurchase.php"
    private val parameters :HashMap<String,String> = HashMap<String,String>()

    constructor(userID : String,product: Product,url:String, listener: Response.Listener<String>?): super(Method.POST,url,listener, null){
        parameters.put("userID",userID);
        parameters.put("pCode",product.pCode);
        parameters.put("price",product.price.toString());
        parameters.put("amount",product.amount.toString());
    }

    override fun getParams(): MutableMap<String, String> {
        return parameters
    }
}