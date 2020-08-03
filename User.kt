package com.example.plan_it

class User {
    var imageURL:String=""
    var Phone:String=""
    var Name:String=""
    var ID:String=""
    var Email:String=""
    var Status:String=""



    constructor(
        imageURL: String,
        Phone: String,
        Name: String,
        ID: String,
        Email: String,
        Status: String
    ) {
        this.imageURL = imageURL
        this.Phone = Phone
        this.Name = Name
        this.ID = ID
        this.Email = Email
        this.Status = Status
    }

    constructor() {}


}