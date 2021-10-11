package com.e.ss_market_reader

class Product {
    var pName : String
    var amount : Int
    var price : Int
    var pCode : String

    constructor(pName: String, amount: Int, price: Int, pCode: String) {
        this.pName = pName
        this.amount = amount
        this.price = price
        this.pCode = pCode
    }
}