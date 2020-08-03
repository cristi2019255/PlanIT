package com.example.plan_it

class Company() {
    var Name:String=""
    var Adress:String=""
    var imageUrl:String="Default"

    constructor(name: String,adress:String,imageUrl:String) : this() {
        this.Name = name
        this.Adress=adress
        this.imageUrl=imageUrl
    }
}