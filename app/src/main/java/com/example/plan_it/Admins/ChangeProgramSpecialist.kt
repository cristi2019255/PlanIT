package com.example.plan_it.Admins

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Admins.Adapter.HourAdapter
import com.example.plan_it.Class.ProgramCompany
import com.example.plan_it.R
import com.google.firebase.database.*

class ChangeProgramSpecialist : AppCompatActivity() {
    private var Luni:RecyclerView?=null
    private var Marti:RecyclerView?=null
    private var Miercuri:RecyclerView?=null
    private var Joi:RecyclerView?=null
    private var Vineri:RecyclerView?=null
    private var Sambata:RecyclerView?=null
    private var Duminica:RecyclerView?=null
    private var Add: Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_program_specialist)

        Luni=findViewById(R.id.Luni_recyclerView_activity_change_program_specialist)
        Luni!!.setHasFixedSize(true)
        Luni!!.setLayoutManager(LinearLayoutManager(baseContext))

        Marti=findViewById(R.id.Marti_recyclerView_activity_change_program_specialist)
        Marti!!.setHasFixedSize(true)
        Marti!!.setLayoutManager(LinearLayoutManager(baseContext))

        Miercuri=findViewById(R.id.Miercuri_recyclerView_activity_change_program_specialist)
        Miercuri!!.setHasFixedSize(true)
        Miercuri!!.setLayoutManager(LinearLayoutManager(baseContext))

        Joi=findViewById(R.id.Joi_recyclerView_activity_change_program_specialist)
        Joi!!.setHasFixedSize(true)
        Joi!!.setLayoutManager(LinearLayoutManager(baseContext))

        Vineri=findViewById(R.id.Vineri_recyclerView_activity_change_program_specialist)
        Vineri!!.setHasFixedSize(true)
        Vineri!!.setLayoutManager(LinearLayoutManager(baseContext))

        Sambata=findViewById(R.id.Sambata_recyclerView_activity_change_program_specialist)
        Sambata!!.setHasFixedSize(true)
        Sambata!!.setLayoutManager(LinearLayoutManager(baseContext))

        Duminica=findViewById(R.id.Duminica_recyclerView_activity_change_program_specialist)
        Duminica!!.setHasFixedSize(true)
        Duminica!!.setLayoutManager(LinearLayoutManager(baseContext))

        Add=findViewById(R.id.Add_working_hour_activity_change_program_specialist)

        var intent=getIntent()
        val CompanyLocality:String=intent.getStringExtra("company locality")
        val CompanyCategory:String=intent.getStringExtra("company category")
        val CompanyName:String=intent.getStringExtra("company name")
        val SpecialistName:String=intent.getStringExtra("specialist name")

        //
        var reference=FirebaseDatabase.getInstance().getReference("companies").child(CompanyLocality)
            .child(CompanyCategory).child(CompanyName).child("Specialists").child(SpecialistName)
            .child("program")
        //

        
        Add!!.setOnClickListener {
            val dialog = AlertDialog.Builder(this@ChangeProgramSpecialist)
            dialog.setTitle("Adauga ore lucratoare")
            dialog.setMessage("Completați toate câmpurile pentru a putea adauga o ora lucratoare")
            dialog.setNegativeButton(
                "Termină"
            ) { dialog, which -> dialog.dismiss() }
            val inflater = LayoutInflater.from(this@ChangeProgramSpecialist)
            val signinview = inflater.inflate(R.layout.add_hour_dialog, null)
            dialog.setView(signinview)
            val StartHour = signinview.findViewById<EditText>(R.id.Hour_start_add_hour_dialog)
            val EndHour = signinview.findViewById<EditText>(R.id.Hour_end_add_hour_dialog)
            val StartMin = signinview.findViewById<EditText>(R.id.StartMinutes_hour_dialog)
            val EndMin = signinview.findViewById<EditText>(R.id.EndMinutes_hour_dialog)

            val Days = signinview.findViewById<Spinner>(R.id.DaySpinner_add_hour_dialog)

            val days = ArrayAdapter(
                this@ChangeProgramSpecialist,
                android.R.layout.simple_list_item_1, resources.getStringArray(R.array.Week_days)
            )
            days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            Days.setAdapter(days)
            Days.setGravity(View.TEXT_ALIGNMENT_CENTER)

            dialog.setPositiveButton("Adaugă", object: DialogInterface.OnClickListener {

                override fun onClick(dialog: DialogInterface, which: Int) {
                    if (varifyHours(StartHour.text.trim().toString().toInt(),StartMin.text.trim().toString().toInt()
                            ,EndHour.text.trim().toString().toInt(),EndMin.text.trim().toString().toInt(),
                            reference,Days.selectedItem.toString())) {

                        val keyHour: String =
                            StartHour.text.trim().toString() + ":" + StartMin.text.trim().toString() +
                                    "-" + EndHour.text.trim().toString() + ":" + EndMin.text.trim().toString()
                        var program1 = HashMap<String, Any>()
                        program1[keyHour] = false
                        reference.child(Days.selectedItem.toString()).updateChildren(program1)

                    }
                }
            })
            dialog.show()
        }

        //
        reference.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                var program:ProgramCompany?=snapshot.getValue(ProgramCompany::class.java)


                if (program!=null) {
                    var hourAdapter = HourAdapter(
                        baseContext, program!!.Luni.keys.toList(), CompanyLocality, CompanyCategory,
                        CompanyName, SpecialistName, "Luni"
                    )
                    Luni!!.setAdapter(hourAdapter)

                    hourAdapter = HourAdapter(
                        baseContext,
                        program!!.Marti.keys.toList(),
                        CompanyLocality,
                        CompanyCategory,
                        CompanyName,
                        SpecialistName,
                        "Marti"
                    )
                    Marti!!.setAdapter(hourAdapter)

                    hourAdapter = HourAdapter(
                        baseContext,
                        program!!.Miercuri.keys.toList(),
                        CompanyLocality,
                        CompanyCategory,
                        CompanyName,
                        SpecialistName,
                        "Miercuri"
                    )
                    Miercuri!!.setAdapter(hourAdapter)

                    hourAdapter = HourAdapter(
                        baseContext, program!!.Joi.keys.toList(), CompanyLocality, CompanyCategory,
                        CompanyName, SpecialistName, "Joi"
                    )
                    Joi!!.setAdapter(hourAdapter)

                    hourAdapter = HourAdapter(
                        baseContext,
                        program!!.Vineri.keys.toList(),
                        CompanyLocality,
                        CompanyCategory,
                        CompanyName,
                        SpecialistName,
                        "Vineri"
                    )
                    Vineri!!.setAdapter(hourAdapter)

                    hourAdapter = HourAdapter(
                        baseContext,
                        program!!.Sambata.keys.toList(),
                        CompanyLocality,
                        CompanyCategory,
                        CompanyName,
                        SpecialistName,
                        "Sambata"
                    )
                    Sambata!!.setAdapter(hourAdapter)

                    hourAdapter = HourAdapter(
                        baseContext,
                        program!!.Duminica.keys.toList(),
                        CompanyLocality,
                        CompanyCategory,
                        CompanyName,
                        SpecialistName,
                        "Duminica"
                    )
                    Duminica!!.setAdapter(hourAdapter)
                }
            }
        })
        //

    }

    private fun varifyHours(StartHH:Int,StartMM:Int,EndHH:Int,EndMM: Int,reference: DatabaseReference,day:String): Boolean {
        if ((StartHH>EndHH)||(StartHH==EndHH && StartMM>=EndMM)){
            Toast.makeText(this@ChangeProgramSpecialist, "Ora de start nu poate fi mai mare de cit cea a finalizarii", Toast.LENGTH_SHORT).show()
            return false
        }
        /*
        if (){
            Toast.makeText(this@ChangeProgramSpecialist, "Va rugam sa introduceti date valide", Toast.LENGTH_SHORT).show()
            return false
        }*/

        return true;
    }
}
