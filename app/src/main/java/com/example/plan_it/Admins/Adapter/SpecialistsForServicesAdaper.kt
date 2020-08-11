package com.example.plan_it.Admins.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.R
import com.google.firebase.database.FirebaseDatabase


class SpecialistsForServicesAdaper(var mContext: Context, var mSpecialists:List<String?>, var CompanyLocality:String
                  , var CompanyCategory:String, var CompanyName:String, var ServiceName:String)
    : RecyclerView.Adapter<SpecialistsForServicesAdaper.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.specialist_for_services_item_admin, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mSpecialists.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
           holder.Name!!.setText(mSpecialists[position])

            var reference =
                FirebaseDatabase.getInstance().getReference("companies").child(CompanyLocality)
                    .child(CompanyCategory).child(CompanyName).child("Services")
                    .child(ServiceName).child("Specialists").child(mSpecialists[position]!!)

            holder.stergeSpecialist!!.setOnClickListener {
                reference.setValue(null)
            }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var Name: TextView?=null
        var stergeSpecialist: Button?=null
        init {
            Name=itemView.findViewById(R.id.Specialist_specialist_for_services_item_admin)
            stergeSpecialist=itemView.findViewById(R.id.Remove_specialist_for_services_item_admin)
        }
    }
}