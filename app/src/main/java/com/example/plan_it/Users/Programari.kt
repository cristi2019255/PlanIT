package com.example.plan_it.Users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Adapter.CompanyAdapter
import com.google.firebase.database.*

import com.example.plan_it.Class.Company
import com.example.plan_it.R
import kotlin.math.min

class Programari : AppCompatActivity() {

    private var list_out:TextView?=null
    private var recyclerView:RecyclerView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_programari)
        var intent:Intent=getIntent()


        val for_search:String=intent.getStringExtra("for_search")
        val locality:String=intent.getStringExtra("locality")
        val category:String=intent.getStringExtra("category")


        list_out=findViewById(R.id.list_companies)
        recyclerView=findViewById(R.id.recicler_view)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setLayoutManager(LinearLayoutManager(baseContext))

        var reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("companies")
            .child(locality).child(category)


        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var list_companies=ArrayList<Company>()
                for (snapshot: DataSnapshot in dataSnapshot!!.children) {
                    val company = snapshot.getValue(Company::class.java)

                    if (isSimilaryByHammingDistance(for_search,company!!.Name)
                        ||company!!.Name.contains(for_search,true)){
                        list_companies!!.add(company)
                    }
                }

                var companyAdapter=CompanyAdapter(baseContext,list_companies,locality,category)
                recyclerView!!.setAdapter(companyAdapter)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun isSimilaryByHammingDistance(s1:String,s2:String):Boolean{
        val min_size=min(s1.length,s2.length)-1
        var mistmatches:Int=0
        for (i in 0..min_size)
            if (!s1.get(i).equals(s2.get(i),true))
                mistmatches++
        return (mistmatches<= min_size*.4)
    }

}
