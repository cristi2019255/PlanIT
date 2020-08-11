package com.example.plan_it.Admins

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.example.plan_it.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddCompany : AppCompatActivity() {
    private var locality:Spinner?=null
    private var category:Spinner?=null
    private var name:EditText?=null
    private var adress:EditText?=null
    private var add: Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_company)
        locality=findViewById(R.id.locality_activity_add_company)
        val Localitati = ArrayAdapter(
            this@AddCompany,
            android.R.layout.simple_list_item_1, resources.getStringArray(R.array.Localitati)
        )
        Localitati.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        locality!!.setAdapter(Localitati)
        locality!!.setGravity(View.TEXT_ALIGNMENT_CENTER)

        category=findViewById(R.id.category_activity_add_company)
        val Categorii = ArrayAdapter(
            this@AddCompany,
            android.R.layout.simple_list_item_1, resources.getStringArray(R.array.Categorii_servicii)
        )
        Categorii.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        category!!.setAdapter(Categorii)
        category!!.setGravity(View.TEXT_ALIGNMENT_CENTER)

        name=findViewById(R.id.name_activity_add_company)
        adress=findViewById(R.id.adress_activity_add_company)
        add=findViewById(R.id.add_activity_add_company)

        var reference=FirebaseDatabase.getInstance().getReference("companies")
        var refuser=FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Companies")
        add!!.setOnClickListener {
            val map=HashMap<String,Any>()
            map["imageUrl"]="Default"
            map["Name"]=name!!.text.toString().trim()
            map["Adress"]=adress!!.text.toString().trim()
            map["Administrator"]=FirebaseAuth.getInstance().currentUser!!.uid
            reference.child(locality!!.selectedItem.toString()).child(category!!.selectedItem.toString())
                .child(name!!.text.toString().trim()).updateChildren(map)
            val map1=HashMap<String,Any>()
            map1[locality!!.selectedItem.toString()+"|"+category!!.selectedItem.toString()+"|"+name!!.text.toString().trim()]=""
            refuser.updateChildren(map1)

            finish()
        }

    }
}
