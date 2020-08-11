package com.example.plan_it.Users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Class.Company
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.bumptech.glide.Glide
import com.example.plan_it.Adapter.ServiceAdapter
import com.example.plan_it.R


class Inscriere : AppCompatActivity() {

    private var CompanyName:TextView?=null
    private var CompanyAdress:TextView?=null
    private var Services:RecyclerView?=null
    private var CompanyImage:ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscriere)
        val intent:Intent=getIntent()

        val locality:String=intent.getStringExtra("company locality")
        val category:String=intent.getStringExtra("company category")
        val name:String=intent.getStringExtra("company name")
        var company:Company?=null


        CompanyName=findViewById(R.id.CompanyName_activity_inscriere)
        CompanyAdress=findViewById(R.id.CompanyAdress_activity_inscriere)
        Services=findViewById(R.id.Servicii_recycler_view_activity_inscriere)
        CompanyImage=findViewById(R.id.profileImage_activity_mainPage_for_admins)
        Services=findViewById(R.id.Servicii_recycler_view_activity_inscriere)
        Services!!.setHasFixedSize(true)
        Services!!.setLayoutManager(LinearLayoutManager(baseContext))


        //database
        var reference=FirebaseDatabase.getInstance().getReference("companies")
            .child(locality).child(category).child(name)
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                company=snapshot.getValue(Company::class.java)

                if (company!!.Services.values.toTypedArray() !=null) {
                    var serviceAdapter = ServiceAdapter(
                        baseContext,
                        company!!.Services.values.toTypedArray(),
                        locality,
                        category,
                        company!!.Name
                    )
                    Services!!.setAdapter(serviceAdapter)
                }

                CompanyName!!.setText(company!!.Name)
                CompanyAdress!!.setText(company!!.Adress)
                if (company!!.imageUrl.equals("Default")) {
                    CompanyImage!!.setImageResource(R.mipmap.ic_launcher)
                } else {
                    Glide.with(baseContext).load(company!!.imageUrl).into(CompanyImage!!)
                }

            }
        })
        //



    }
}
