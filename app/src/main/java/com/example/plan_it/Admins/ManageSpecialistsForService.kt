package com.example.plan_it.Admins

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Admins.Adapter.SpecialistAdapter
import com.example.plan_it.Admins.Adapter.SpecialistsForServicesAdaper
import com.example.plan_it.R
import com.google.firebase.database.*
import java.util.HashMap

class ManageSpecialistsForService : AppCompatActivity() {
    private var SpecialistsList: RecyclerView?=null
    private var Add: Button?=null
    private var AllSpecialistSpinner: Spinner?=null
    var CompanyLocalityS:String=""
    var CompanyCategoryS:String=""
    var CompanyNameS:String=""
    var ServiceNameS:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_managa_specialists_for_service)

        Add=findViewById(R.id.Add_button_activity_manage_specialists_for_service)
        AllSpecialistSpinner=findViewById(R.id.AllSpecialists_spinner_activity_manage_specialists_for_service)
        SpecialistsList= findViewById(R.id.Specialists_recycler_view_activity_manage_specialist_for_service)
        SpecialistsList!!.setHasFixedSize(true)
        SpecialistsList!!.setLayoutManager(LinearLayoutManager(baseContext))


        var intent=getIntent()
        CompanyLocalityS=intent.getStringExtra("company locality")
        CompanyCategoryS=intent.getStringExtra("company category")
        CompanyNameS=intent.getStringExtra("company name")
        ServiceNameS=intent.getStringExtra("service name")

        var reference=FirebaseDatabase.getInstance().getReference("companies").child(CompanyLocalityS)
            .child(CompanyCategoryS).child(CompanyNameS)

        reference.child("Specialists").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                val t = object : GenericTypeIndicator<HashMap<String, Any>>() {}

                if (snapshot.getValue(t) != null) {
                    val specialistAdapter = ArrayAdapter(
                        this@ManageSpecialistsForService,
                        android.R.layout.simple_list_item_1,
                        snapshot.getValue(t)!!.keys.toTypedArray()
                    )

                    AllSpecialistSpinner!!.setGravity(View.TEXT_ALIGNMENT_CENTER)
                    specialistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    AllSpecialistSpinner!!.setAdapter(specialistAdapter)
                }
            }
        })

        reference.child("Services").child(ServiceNameS).child("Specialists").addValueEventListener(
            object :ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    val t = object : GenericTypeIndicator<java.util.HashMap<String, Any>>() {}
                    if (snapshot.getValue(t)!=null) {
                        var specialistAdapter =
                            SpecialistsForServicesAdaper(
                                baseContext,
                                snapshot.getValue(t)!!.keys.toList(),
                                CompanyLocalityS,
                                CompanyCategoryS,
                                CompanyNameS,
                                ServiceNameS
                            )
                        SpecialistsList!!.setAdapter(specialistAdapter)

                    }
                }
            })

        Add!!.setOnClickListener {
           var map=HashMap<String,Any>()
           map[AllSpecialistSpinner!!.selectedItem.toString()]=""
           reference.child("Services").child(ServiceNameS).child("Specialists").updateChildren(map)
        }
    }
}
