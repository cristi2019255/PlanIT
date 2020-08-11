package com.example.plan_it.Class

class ProgramCompany() {
    var Luni=HashMap<String,Any>()
    var Marti=HashMap<String,Any>()
    var Miercuri=HashMap<String,Any>()
    var Joi=HashMap<String,Any>()
    var Vineri=HashMap<String,Any>()
    var Sambata=HashMap<String,Any>()
    var Duminica=HashMap<String,Any>()

    constructor(
        Luni: HashMap<String, Any>,
        Marti: HashMap<String, Any>,
        Miercuri: HashMap<String, Any>,
        Joi: HashMap<String, Any>,
        Vineri: HashMap<String, Any>,
        Sambata: HashMap<String, Any>,
        Duminica: HashMap<String, Any>
    ):this() {
        this.Luni = Luni
        this.Marti = Marti
        this.Miercuri = Miercuri
        this.Joi = Joi
        this.Vineri = Vineri
        this.Sambata = Sambata
        this.Duminica = Duminica
    }

    override fun toString(): String {
        return "ProgramCompany(Luni=${Luni}, Marti=$Marti, Miercuri=$Miercuri, Joi=$Joi, Vineri=$Vineri, Sambata=$Sambata, Duminica=$Duminica)"
    }

}