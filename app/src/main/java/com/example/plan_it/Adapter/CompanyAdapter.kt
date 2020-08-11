package com.example.plan_it.Adapter

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plan_it.Class.Company
import com.example.plan_it.Users.Inscriere
import com.example.plan_it.R


class CompanyAdapter(var mContext:Context,var mCompanies:ArrayList<Company>,var Locality:String,var Category:String) : RecyclerView.Adapter<CompanyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.company_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mCompanies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name!!.setText(mCompanies.get(position).Name)
        holder.adress!!.setText(mCompanies.get(position).Adress)
        var company: Company =mCompanies.get(position)
        if (company.imageUrl.equals("Default")) {
            holder.profile_Image!!.setImageResource(R.mipmap.ic_launcher)
        } else {
            Glide.with(mContext).load(company.imageUrl).into(holder.profile_Image!!)
        }

        holder.planifica!!.setOnClickListener {
            var intent=Intent(mContext, Inscriere::class.java)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("company name",company.Name)
            intent.putExtra("company adress",company.Adress)
            intent.putExtra("company imageUrl",company.imageUrl)
            intent.putExtra("company locality",Locality)
            intent.putExtra("company category",Category)

            mContext.startActivity(intent)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView?=null
        var adress:TextView?=null
        var planifica:Button?=null
        var profile_Image:ImageView?=null
        init {
          name=itemView.findViewById(R.id.CompanyName_company_item)
          adress=itemView.findViewById(R.id.Adress_company_item)
          planifica=itemView.findViewById(R.id.Planifica_company_item)
            profile_Image=itemView.findViewById(R.id.company_image_in_company_item)
        }
    }


}