package com.example.plan_it.Admins

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.plan_it.R
import com.google.firebase.database.FirebaseDatabase

class AddService : AppCompatActivity() {
    private var Add: Button?=null
    private var Name:EditText?=null
    private var Price:EditText?=null
    private var Time:EditText?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service)
        var intent=getIntent()

        Add=findViewById(R.id.Add_activity_add_service)
        Name=findViewById(R.id.Name_activity_add_service)
        Price=findViewById(R.id.Price_activity_add_service)
        Time=findViewById(R.id.Time_activity_add_service)

        var reference=FirebaseDatabase.getInstance().getReference("companies")
            .child(intent.getStringExtra("company locality")).child(intent.getStringExtra("company category"))
            .child(intent.getStringExtra("company name")).child("Services")

        Add!!.setOnClickListener {
            var reference1=reference.child(Name!!.text.trim().toString())
            val map=HashMap<String,Any>()
            map["Name"]=Name!!.text.trim().toString()
            map["Pret"]=Price!!.text.trim().toString()
            map["Time"]=Time!!.text.trim().toString()
            reference1.updateChildren(map)
            finish()
        }
    }
}
