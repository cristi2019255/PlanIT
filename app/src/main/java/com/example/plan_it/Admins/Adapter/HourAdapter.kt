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



class HourAdapter(var mContext: Context, var mHours:List<String?>, var CompanyLocality:String
                     , var CompanyCategory:String, var CompanyName:String,var SpecialistName:String,var Day:String) : RecyclerView.Adapter<HourAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.hour_item_admin, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mHours.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mHours!=null) {
            holder.hours!!.setText(mHours[position])

            var reference =
                FirebaseDatabase.getInstance().getReference("companies").child(CompanyLocality)
                    .child(CompanyCategory).child(CompanyName).child("Specialists")
                    .child(SpecialistName).child("Program").child(Day).child(mHours[position]!!)

            holder.stergeOra!!.setOnClickListener {
                reference.setValue(null)
            }

        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hours: TextView?=null
        var stergeOra: Button?=null
        init {
            hours=itemView.findViewById(R.id.Hours_hour_item_admin)
            stergeOra=itemView.findViewById(R.id.Remove_hour_item_admin)
        }
    }
}