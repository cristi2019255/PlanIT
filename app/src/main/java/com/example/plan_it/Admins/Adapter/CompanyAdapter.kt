package com.example.plan_it.Admins.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plan_it.Admins.AdministrateCompany
import com.example.plan_it.Class.Company
import com.example.plan_it.R
import com.example.plan_it.Users.Inscriere
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CompanyAdapter(var mContext: Context,var mDetails:List<String>) : RecyclerView.Adapter<CompanyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.company_admin_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDetails.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var reference1= FirebaseDatabase.getInstance().getReference("companies")
            .child(mDetails[position].split("|")[0]).child(mDetails[position].split("|")[1])
            .child(mDetails[position].split("|")[2])
        reference1.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                var company:Company?=snapshot.getValue(Company::class.java)
                holder.name!!.setText(company!!.Name)
                holder.adress!!.setText(company!!.Adress)

                if (company.imageUrl.equals("Default")) {
                    holder.profile_Image!!.setImageResource(R.mipmap.ic_launcher)
                } else {
                    Glide.with(mContext).load(company!!.imageUrl).into(holder.profile_Image!!)
                }
            }
        })


        holder.planifica!!.setOnClickListener {
            var intent= Intent(mContext, AdministrateCompany::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("company locality",mDetails.get(position).split("|")[0])
            intent.putExtra("company category",mDetails.get(position).split("|")[1])
            intent.putExtra("company name",mDetails.get(position).split("|")[2])
            mContext.startActivity(intent)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView?=null
        var adress: TextView?=null
        var planifica: Button?=null
        var profile_Image: ImageView?=null
        init {
            name=itemView.findViewById(R.id.CompanyName_company_admin_item)
            adress=itemView.findViewById(R.id.Adress_company_admin_item)
            planifica=itemView.findViewById(R.id.Planifica_company_admin_item)
            profile_Image=itemView.findViewById(R.id.company_image_in_company_admin_item)
        }
    }
}