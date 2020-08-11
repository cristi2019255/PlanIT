package com.example.plan_it.Class

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.DataSnapshot

class Company (){
    var Name:String=""
    var Administrator:String=""
    var Adress:String=""
    var imageUrl:String="Default"
    var Specialists=HashMap<String,Specialist?>()
    var Services=HashMap<String,Service?>()
    constructor(
        Name: String,
        Adress: String,
        imageUrl: String,
        Services:HashMap<String,Service?>,
        Administrator:String
    ):this() {
        this.Name = Name
        this.Adress = Adress
        this.imageUrl = imageUrl
        this.Services=Services
        this.Administrator=Administrator
    }

    override fun toString(): String {
        return "Company(Name='$Name', Adress='$Adress', imageUrl='$imageUrl', Specialists=$Specialists)"
    }


}