package com.example.plan_it.Admins

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Admins.Adapter.CompanyAdapter
import com.example.plan_it.Class.Company
import com.example.plan_it.Class.User
import com.example.plan_it.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyCompanies : AppCompatActivity() {

    private var Companies:RecyclerView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_companies)
        Companies=findViewById(R.id.Companies_recyclerView_activity_my_companies)
        Companies!!.setHasFixedSize(true)
        Companies!!.setLayoutManager(LinearLayoutManager(baseContext))

        var list_companies=ArrayList<Company>()
        var list_details=ArrayList<String>()

        //database
        var reference=FirebaseDatabase.getInstance().getReference("Users")
            .child(intent.getStringExtra("user id")!!)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user1: User? = dataSnapshot.getValue(User::class.java)
                var companies=user1!!.Companies!!.keys.toList()
                var companyAdapter = CompanyAdapter(baseContext,  companies)
                Companies!!.setAdapter(companyAdapter)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        //database



    }
}
