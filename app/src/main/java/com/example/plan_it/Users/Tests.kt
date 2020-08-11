package com.example.plan_it.Users

import java.util.*

    fun main(args: Array<String>) {

        println(DataBeetween("10|08|2020","18|08|2020","11|09|2020"))

    }

    fun DataBeetween(start:String,end:String,data:String):Boolean{
        var DateStart= Date(
            start.split("|")[1]+"/"+
                    start.split("|")[0]+"/"+
                    start.split("|")[2])
        var DateEnd= Date(
            end.split("|")[1]+"/"+
                    end.split("|")[0]+"/"+
                    end.split("|")[2])
        var DateNow= Date(
            data.split("|")[1]+"/"+
                    data.split("|")[0]+"/"+
                    data.split("|")[2])

        if (DateStart.compareTo(DateNow)<=0 && DateNow.compareTo(DateEnd)<=0)
            return true
        return false
    }
