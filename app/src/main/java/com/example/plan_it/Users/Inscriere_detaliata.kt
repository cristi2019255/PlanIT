package com.example.plan_it.Users

import android.app.DatePickerDialog
import android.icu.text.DateTimePatternGenerator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Adapter.ProgramAdapter
import com.example.plan_it.Class.Service
import com.example.plan_it.Class.Specialist
import com.example.plan_it.Fragment.DatePickerFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plan_it.R


class Inscriere_detaliata : AppCompatActivity(), DatePickerDialog.OnDateSetListener  {

    private var ServiceName:TextView?=null
    private var ServiceTime:TextView?=null
    private var ServicePrice:TextView?=null
    private var ServiceDataChosen:TextView?=null
    private var SpecialistsSpiner:Spinner?=null
    private var ProgramSpecialist:RecyclerView?=null
    private var ChoseData: Button?=null
    internal var DatePicked:String = SimpleDateFormat("dd|MM|yyyy", Locale.getDefault()).format(Date())
    internal var dayOfWeek:Int=1
    private var service1:Service?=null
    private var specialist1:Specialist?=null
    private var company_locality:String=""
    private var company_category:String=""
    private var company_name:String=""
    private var service_name:String=""
    private var user:String=""
    internal var programDay=HashMap<String,Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscriere_detaliata)
        ServiceName=findViewById(R.id.ServiceName_activity_inscriere_detaliata)
        ServiceTime=findViewById(R.id.ServiceTime_activity_inscriere_detaliata)
        ServicePrice=findViewById(R.id.ServicePret_activity_inscriere_detaliata)
        ServiceDataChosen=findViewById(R.id.Data_out_activity_incriere_detaliata)

        ServiceDataChosen!!.setText(SimpleDateFormat("dd|MM|yyyy", Locale.getDefault()).format(Date()))
        SpecialistsSpiner=findViewById(R.id.Specialists_spinner_activity_inscriere_detaliata)

        ProgramSpecialist=findViewById(R.id.ProgramSpecialist_recyclerViewer_activity_inscriere_detaliata)
        ProgramSpecialist!!.setHasFixedSize(true)
        ProgramSpecialist!!.setLayoutManager(LinearLayoutManager(baseContext))

        ChoseData=findViewById(R.id.ChoseDate_activity_inscriere_detaliata)

        SpecialistsSpiner!!.setGravity(View.TEXT_ALIGNMENT_CENTER)

        val intent=getIntent()
        company_locality=intent.getStringExtra("company locality")
        company_category=intent.getStringExtra("company category")
        company_name=intent.getStringExtra("company name")
        service_name=intent.getStringExtra("service name")


        //database
        user=FirebaseAuth.getInstance().currentUser!!.uid
        //database

        ServiceName!!.setText(service_name)
        ChoseData!!.setOnClickListener{
            val datePicker = DatePickerFragment()
            datePicker.show(supportFragmentManager, "date picker")
        }

        SpecialistsSpiner!!.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                afiseazaProgram(selectedItem,DatePicked)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        })


        //database for service
        var reference= FirebaseDatabase.getInstance().getReference("companies")
            .child(company_locality).child(company_category).child(company_name).child("Services")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                var service:Service?=null
                for (snapshot1:DataSnapshot in snapshot.children){
                    service=snapshot1.getValue(Service::class.java)
                    if (service!!.Name.equals(service_name)){
                        service1=service
                        break
                    }
                }

                ServiceTime!!.setText(service!!.Time+" min")
                ServicePrice!!.setText(service!!.Pret+" RON")

                val specialists = ArrayAdapter(
                    this@Inscriere_detaliata,
                    android.R.layout.simple_list_item_1,
                    service!!.Specialists.keys.toTypedArray()
                )
                specialists.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                SpecialistsSpiner!!.setAdapter(specialists)


            }
        })
        //
    }

    override fun onDateSet(view: DatePicker?, year: Int,month: Int,dayOfMonth: Int) {

        val currentDate = SimpleDateFormat("dd|MM|yyyy", Locale.getDefault()).format(Date())
        var curentday:Int=currentDate.split("|")[0].toInt()
        var curentmonth:Int=currentDate.split("|")[1].toInt()
        var curentyear:Int=currentDate.split("|")[2].toInt()

        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.time)
        var day:String=""
        var months:String=""
        if (dayOfMonth<10){
            day="0$dayOfMonth"
        }else day=dayOfMonth.toString()
        if ((month+1)<10){
            months="0${month+1}"
        } else months=(month+1).toString()

        if ((year<curentyear) ||(year==curentyear && month+1<curentmonth) ||(year==curentyear && (month+1==curentmonth) && (dayOfMonth<curentday))){
            DatePicked=currentDate
            Toast.makeText(this@Inscriere_detaliata, "Nu puteți să vă programați pe o dată care a trecut.", Toast.LENGTH_SHORT).show()
            var curentdays=curentday.toString()
            if (curentday<10) curentdays="0$curentdays"
            var curentmonths=curentmonth.toString()
            if (curentmonth<10) curentmonths="0$curentmonths"
            ServiceDataChosen!!.setText("$curentdays|$curentmonths|$curentyear")
        }else {
            DatePicked = "$day|$months|$year"
            Toast.makeText(baseContext, currentDateString, Toast.LENGTH_SHORT).show()
            ServiceDataChosen!!.setText("$day|$months|$year")
        }

        afiseazaProgram(SpecialistsSpiner!!.selectedItem.toString(),ServiceDataChosen!!.text.toString())
    }



    fun afiseazaProgram(selectedItem:String,DatePickednew:String){
        var DatePickednew1=DatePickednew
        var reference1= FirebaseDatabase.getInstance().getReference("companies")
            .child(company_locality).child(company_category).child(company_name).child("Specialists")
            .child(selectedItem)

        reference1.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                var specialist:Specialist? = snapshot.getValue(Specialist::class.java)


                var d=Date(
                    DatePickednew1.split("|")[1]+"/"+
                            DatePickednew1.split("|")[0]+"/"+
                            DatePickednew1.split("|")[2])
                var c:Calendar= Calendar.getInstance()
                c.setTime(d)
                dayOfWeek=c.get(Calendar.DAY_OF_WEEK)-1

                when(dayOfWeek){
                    1->programDay=specialist!!.Program!!.Luni
                    2->programDay=specialist!!.Program!!.Marti
                    3->programDay=specialist!!.Program!!.Miercuri
                    4->programDay=specialist!!.Program!!.Joi
                    5->programDay=specialist!!.Program!!.Vineri
                    6->programDay=specialist!!.Program!!.Sambata
                    7->programDay=specialist!!.Program!!.Duminica
                }

                var concedii=specialist!!.disponibility.keys.toList()
                var inscrieri=specialist!!.ProgramariSpecialist.keys.toList()
                var hours=programDay.keys.toList()


                if (inConcediu(concedii,DatePickednew1)){
                    Toast.makeText(this@Inscriere_detaliata,
                        "Nu puteți să vă programați pe acesta dată,specialistul nu este disponibil.\nIncercati alt specialist",
                        Toast.LENGTH_LONG).show()
                    var map=HashMap<String,String>()
                    var programAdapter =
                        ProgramAdapter(
                            baseContext,map.keys.toList() , company_locality,
                            company_category, company_name,
                            specialist!!.Name!!, DatePicked
                            , service1!!.Time, user
                        )
                    ProgramSpecialist!!.setAdapter(programAdapter)

                } else {


                    for (i in 0..(inscrieri.size - 1)) {
                        if (inscrieri[i].split(" ")[0].equals(DatePickednew)) {
                            for (j in 0..(hours.size - 1)) {
                                if (hours[j].contains(inscrieri[i].split(" ")[1])) {
                                    programDay.keys.remove(hours[j])
                                }
                            }
                        }
                    }


                    var programAdapter =
                        ProgramAdapter(
                            baseContext, programDay.keys.toList(), company_locality,
                            company_category, company_name,
                            specialist!!.Name!!, DatePicked
                            , service1!!.Time, user
                        )
                    ProgramSpecialist!!.setAdapter(programAdapter)
                }
            }
        })

    }

    fun inConcediu(concedii:List<String>,data:String):Boolean{
        for (i in 0..(concedii.size-1)){
            println(concedii[i])
            if (DataBeetween(concedii[i].split("-")[0],concedii[i].split("-")[1],data)){
                return true
            }
        }
        return false
    }

    fun DataBeetween(start:String,end:String,data:String):Boolean{
        var DateStart=Date(
            start.split("|")[1]+"/"+
                    start.split("|")[0]+"/"+
                    start.split("|")[2])
        var DateEnd=Date(
                 end.split("|")[1]+"/"+
                    end.split("|")[0]+"/"+
                    end.split("|")[2])
        var DateNow=Date(
                 data.split("|")[1]+"/"+
                    data.split("|")[0]+"/"+
                    data.split("|")[2])

        if (DateStart.compareTo(DateNow)<=0 && DateNow.compareTo(DateEnd)<=0) {
            println(DateStart.toString())
            println(DateEnd.toString())
            println(DateNow.toString())
            return true
        }else {
            println(DateStart.toString())
            println(DateEnd.toString())
            println(DateNow.toString())
            return false
        }
    }
}