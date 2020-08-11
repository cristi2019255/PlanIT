package com.example.plan_it.Admins

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Admins.Adapter.DateDisponibilityAdapter
import com.example.plan_it.Fragment.DatePickerFragment
import com.example.plan_it.R
import com.google.firebase.database.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ChangeDisponibilitySpecialist : AppCompatActivity(), DatePickerDialog.OnDateSetListener{
    private var Dates:RecyclerView?=null
    private var DataStart:TextView?=null
    private var DataEnd:TextView?=null
    private var AddStart:Button?=null
    private var AddEnd:Button?=null
    private var AddData:Button?=null
    private var Motiv:EditText?=null

    private var indicator_for_date_pickers:Boolean=true //true->startdate date picker false->enddate date picker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_disponibility_specialist)

        Dates=findViewById(R.id.Dates_activity_chage_disponiblity_spceialist)
        Dates!!.setHasFixedSize(true)
        Dates!!.setLayoutManager(LinearLayoutManager(baseContext))

        DataStart=findViewById(R.id.Data_Start_activity_change_disponibility)
        DataEnd=findViewById(R.id.Data_End_activity_change_disponibility)
        AddStart=findViewById(R.id.Add_data_start_activity_change_disponibility)
        AddEnd=findViewById(R.id.Add_data_End_activity_change_disponibility)
        AddData=findViewById(R.id.Add_data_disponibility_activity_change_disponibility)
        Motiv=findViewById(R.id.Motiv_activity_change_disponibility_specialist)

        DataStart!!.setText(SimpleDateFormat("dd|MM|yyyy", Locale.getDefault()).format(Date()))
        DataEnd!!.setText(SimpleDateFormat("dd|MM|yyyy", Locale.getDefault()).format(Date()))

        AddStart!!.setOnClickListener {
            indicator_for_date_pickers=true
            val datePicker = DatePickerFragment()
            datePicker.show(supportFragmentManager, "date picker")
        }


        AddEnd!!.setOnClickListener {
            indicator_for_date_pickers=false
            val datePicker = DatePickerFragment()
            datePicker.show(supportFragmentManager, "date picker")
        }

        var intent=getIntent()
        val CompanyLocality:String=intent.getStringExtra("company locality")
        val CompanyCategory:String=intent.getStringExtra("company category")
        val CompanyName:String=intent.getStringExtra("company name")
        val SpecialistName:String=intent.getStringExtra("specialist name")

        //
        var reference= FirebaseDatabase.getInstance().getReference("companies").child(CompanyLocality)
            .child(CompanyCategory).child(CompanyName).child("Specialists").child(SpecialistName)
            .child("disponibility")
        //


        AddData!!.setOnClickListener {
            var map=HashMap<String,Any>()
            val start:String=DataStart!!.text.toString().trim()
            val end:String=DataEnd!!.text.toString().trim()
            var yyyyend:Int=end.split("|")[2].toInt()
            var mmend:Int=end.split("|")[1].toInt()
            var ddend:Int=end.split("|")[0].toInt()
            var yyyystart:Int=start.split("|")[2].toInt()
            var mmstart:Int=start.split("|")[1].toInt()
            var ddstart:Int=start.split("|")[0].toInt()

            if ((yyyyend<yyyystart)||(yyyystart==yyyyend && mmend<mmstart)
                ||(yyyystart==yyyyend && mmend==mmstart && ddend<=ddstart)){
                Toast.makeText(baseContext, "Date invalide", Toast.LENGTH_SHORT).show()
            }else {
                map["$start-$end"] = Motiv!!.text.toString().trim()
                reference.updateChildren(map)
                Toast.makeText(baseContext, "Adaugat", Toast.LENGTH_SHORT).show()
            }
        }


        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                val t = object : GenericTypeIndicator<HashMap<String, String>>() {}
                var map=HashMap<String,String>()
                if (snapshot.getValue(t)!=null){
                    map=snapshot.getValue(t)!!
                }

                var dateDisponibility=DateDisponibilityAdapter(
                    baseContext,
                    map.keys.toList(),
                    map.values.toList(),
                    CompanyLocality,
                    CompanyCategory,
                    CompanyName,
                    SpecialistName)
                Dates!!.setAdapter(dateDisponibility)
            }
        })
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

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

        Toast.makeText(baseContext, currentDateString, Toast.LENGTH_SHORT).show()
        if (indicator_for_date_pickers){
            DataStart!!.setText("$day|$months|$year")
        }else{
            DataEnd!!.setText("$day|$months|$year")
        }
    }

}