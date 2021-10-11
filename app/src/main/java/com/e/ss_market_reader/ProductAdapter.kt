package com.e.ss_market_reader

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ProductAdapter : BaseAdapter {
    private var list : List<Product>
    private var context : Context

    constructor(list: List<Product>, context: Context){
        this.list = list
        this.context = context
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(index: Int): Any {
        return list.get(index)
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getView(index: Int, view: View?, viewGroup: ViewGroup?): View {
        val v = View.inflate(context,R.layout.product_item,null)

        val pName = v.findViewById<TextView>(R.id.productName)
        val amount = v.findViewById<TextView>(R.id.amount)
        val price = v.findViewById<TextView>(R.id.productPrice)

        pName.text = list.get(index).pName
        amount.text = list.get(index).amount.toString() +" 개"
        price.text = list.get(index).price.toString() + " 원"

        return v
    }
}