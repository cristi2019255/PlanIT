package com.example.plan_it.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.plan_it.R
import com.google.firebase.database.FirebaseDatabase
import java.util.HashMap

class ProgramAdapter (var mContext: Context, var mProgram:List<String>
                      ,val CompanyLocality:String,val CompanyCategory:String,val CompanyName:String
                      ,val Specialist:String,val Data:String,val Time:String,val User:String)
    : RecyclerView.Adapter<ProgramAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.program_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mProgram.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.Start!!.setText(mProgram[position]!!.split("-")[0])
        holder.End!!.setText(mProgram[position]!!.split("-")[1])

        holder.Inscrie!!.setOnClickListener {
            //database
            var reference1= FirebaseDatabase.getInstance().getReference("companies")
                .child(CompanyLocality).child(CompanyCategory).child(CompanyName).child("Specialists")
                .child(Specialist).child("ProgramariSpecialist")
            var map = HashMap<String, Any>()
            val hour_start:String=mProgram.get(position).split("-")[0]
            var Data1=Data+" "+hour_start.substring(1)+" "+Time
            map[Data1] = User
            reference1.updateChildren(map)
            //

        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var Start: TextView? = null
        var End: TextView? = null
        var Inscrie: Button? = null

        init {
            Start = itemView.findViewById(R.id.Start_program_item)
            End = itemView.findViewById(R.id.End_program_item)
            Inscrie = itemView.findViewById(R.id.Inscriere_program_item)
        }
    }
}