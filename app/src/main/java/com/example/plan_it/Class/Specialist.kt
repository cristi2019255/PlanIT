package com.example.plan_it.Class


class Specialist() {
    var Name:String?=null
    var Program:ProgramCompany?=null
    var ProgramariSpecialist=HashMap<String,String?>()
    var disponibility=HashMap<String,String?>()

    constructor(Name:String?,Program: ProgramCompany?,
                ProgramariSpecialist: HashMap<String, String?>,Disponibilitate: HashMap<String, String?>):this() {
        this.Program = Program
        this.ProgramariSpecialist = ProgramariSpecialist
        this.Name=Name
        this.disponibility=Disponibilitate
    }

    override fun toString(): String {
        return "Specialist(Name=$Name, Program=$Program, ProgramariSpecialist=$ProgramariSpecialist)"
    }


}