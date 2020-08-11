package com.example.plan_it.Admins.Adapter

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Admins.ChangeProgramSpecialist
import com.example.plan_it.Admins.ManageSpecialistsForService
import com.example.plan_it.Class.Service
import com.example.plan_it.R
import com.google.firebase.database.*
import kotlin.coroutines.coroutineContext


class ServiceAdapter(var mContext: Context, var mServices:List<Service?>,var CompanyLocality:String
                     ,var CompanyCategory:String,var CompanyName:String) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.service_item_admin, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mServices.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name!!.setText(mServices[position]!!.Name)
        holder.pret!!.setText(mServices[position]!!.Pret)
        holder.time!!.setText(mServices.get(position)!!.Time)
        var reference=FirebaseDatabase.getInstance().getReference("companies").child(CompanyLocality)
            .child(CompanyCategory).child(CompanyName).child("Services").child(mServices[position]!!.Name)

        reference.child("Specialists").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                val t = object : GenericTypeIndicator<java.util.HashMap<String, Any>>() {}

                if (snapshot.getValue(t)!=null) {
                    val specialistAdapter = ArrayAdapter(
                        mContext,
                        android.R.layout.simple_list_item_1,
                        snapshot.getValue(t)!!.keys.toTypedArray()
                    )

                    holder.specialistsSpinner!!.setGravity(View.TEXT_ALIGNMENT_CENTER)
                    specialistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    holder.specialistsSpinner!!.setAdapter(specialistAdapter)
                }
            }
        })

        holder.modifica!!.setOnClickListener {
            val map=HashMap<String,Any>()
            map["Pret"]=holder.pret!!.text.trim().toString()
            map["Time"]=holder.time!!.text.trim().toString()
            map["Name"]=mServices[position]!!.Name
            reference.updateChildren(map)
        }

        holder.specialists!!.setOnClickListener {
            var intent=Intent(mContext, ManageSpecialistsForService::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("company locality",CompanyLocality)
            intent.putExtra("company category",CompanyCategory)
            intent.putExtra("company name",CompanyName)
            intent.putExtra("service name",mServices[position]!!.Name)
            mContext.startActivity(intent)
        }

        holder.stergeServiciu!!.setOnClickListener{
         /*   val dialog = AlertDialog.Builder(ContextThemeWrapper(mContext, R.style.myDialog))
            dialog.setTitle("Ștergere serviciu")
            dialog.setMessage("Sunteți sigur că doriți să ștergeți acest serviciu din lista de servicii?")
            dialog.setNegativeButton(
                "Termină"
            ) { dialog, which -> dialog.dismiss() }
            dialog.setPositiveButton("Șterge", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                */    reference.setValue(null)
        /*
                }
            })

            dialog.show()
        */
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView?=null
        var pret: TextView?=null
        var time: TextView?=null
        var modifica: Button?=null
        var specialists:Button?=null
        var stergeServiciu: Button?=null
        var specialistsSpinner:Spinner?=null

        init {
            name=itemView.findViewById(R.id.Name_service_item_admin)
            pret=itemView.findViewById(R.id.Price_service_item_admin)
            time=itemView.findViewById(R.id.time_service_item_admin)
            modifica=itemView.findViewById(R.id.Change_service_item_admin)
            specialists=itemView.findViewById(R.id.GestionateSpecialists_service_item_admin)
            specialistsSpinner=itemView.findViewById(R.id.SpecialistsSpinner_service_item_admin)
            stergeServiciu=itemView.findViewById(R.id.Remove_service_item_admin)
        }
    }
}