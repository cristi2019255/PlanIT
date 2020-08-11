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


class DateDisponibilityAdapter(var mContext: Context, var mDates:List<String?>,var mReasons:List<String>, var CompanyLocality:String
                        , var CompanyCategory:String, var CompanyName:String,var SpecialistName:String) : RecyclerView.Adapter<DateDisponibilityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.dates_disponibility_item_admin, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDates.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.Name!!.setText(mDates[position])
        holder.Reason!!.setText(mReasons[position])

        var reference =
            FirebaseDatabase.getInstance().getReference("companies").child(CompanyLocality)
                .child(CompanyCategory).child(CompanyName).child("Specialists")
                .child(SpecialistName).child("disponibility").child(mDates[position]!!)

        holder.stergeData!!.setOnClickListener{
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

        var Name:TextView?=null
        var Reason:TextView?=null
        var stergeData: Button?=null

        init {
            Name=itemView.findViewById(R.id.Data_dates_disponibility_item_admin)
            Reason=itemView.findViewById(R.id.Reason_dates_disponibility_item_admin)
            stergeData=itemView.findViewById(R.id.Remove_dates_disponibility_item_admin)
        }
    }
}