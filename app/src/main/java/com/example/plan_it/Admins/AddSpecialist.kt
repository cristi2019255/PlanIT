package com.example.plan_it.Admins

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.plan_it.R
import com.google.firebase.database.FirebaseDatabase

class AddSpecialist : AppCompatActivity() {
    private var Add: Button?=null
    private var Name: EditText?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_specialist)

        var intent=getIntent()
        Add=findViewById(R.id.Add_activity_add_specialist)
        Name=findViewById(R.id.Name_activity_add_specialist)

        var reference= FirebaseDatabase.getInstance().getReference("companies")
            .child(intent.getStringExtra("company locality")).child(intent.getStringExtra("company category"))
            .child(intent.getStringExtra("company name")).child("Specialists")

        Add!!.setOnClickListener {
            var reference1=reference.child(Name!!.text.trim().toString())
            val map=HashMap<String,Any>()
            map["Name"]=Name!!.text.trim().toString()
            reference1.updateChildren(map)
            finish()
        }


    }
}
