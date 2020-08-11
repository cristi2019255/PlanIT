package com.example.plan_it.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Class.Service
import com.example.plan_it.Users.Inscriere_detaliata
import com.example.plan_it.R

class ServiceAdapter (var mContext: Context, var mServices:Array<Service?>
                      ,var CompanyLocality:String,var CompanyCategory:String,var CompanyName:String)
    : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.service_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mServices.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.name!!.setText(mServices.get(position)!!.Name)
            holder.pret!!.setText(mServices.get(position)!!.Pret)

            holder.comanda!!.setOnClickListener {
                var intent = Intent(mContext, Inscriere_detaliata::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("company name", CompanyName)
                intent.putExtra("company locality", CompanyLocality)
                intent.putExtra("company category", CompanyCategory)
                intent.putExtra("service name", mServices.get(position)!!.Name)
                mContext.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null
        var pret: TextView? = null
        var comanda: Button? = null

        init {
            name = itemView.findViewById(R.id.Name_service_item)
            pret = itemView.findViewById(R.id.Price_service_item)
            comanda = itemView.findViewById(R.id.Comanda_service_item)
        }
    }
}