package com.e.ss_market_reader

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

class PurchaseRequest : StringRequest{

    private val parameters :HashMap<String,String> = HashMap<String,String>()

    constructor(userID : String,product: Product,listener: Response.Listener<String>?): super(Method.POST,URL,listener, null){
        parameters.put("userID",userID);
        parameters.put("pCode",product.pCode);
        parameters.put("price",""+product.price);
        parameters.put("amount",""+product.amount);
    }

    override fun getParams(): MutableMap<String, String> {
        return parameters
    }

    companion object{
        const val URL = "https://ctg1770.cafe24.com/SC/S_C_AddPurchase.php"
    }
}