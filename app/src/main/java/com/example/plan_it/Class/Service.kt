package com.example.plan_it.Class

class Service() {
    var Name:String=""
    var Pret:String=""
    var Specialists=HashMap<String,String>()
    var Time:String=""

    constructor(Name: String, Pret: String, Specialists: HashMap<String, String>,Time:String):this() {
        this.Name = Name
        this.Pret = Pret
        this.Specialists = Specialists
        this.Time=Time
    }

    override fun toString(): String {
        return "Service(Name='$Name', Pret='$Pret', Specialists=$Specialists, Time='$Time')"
    }


}