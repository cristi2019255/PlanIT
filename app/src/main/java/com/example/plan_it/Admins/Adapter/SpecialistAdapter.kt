package com.example.plan_it.Admins.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plan_it.Admins.ChangeDisponibilitySpecialist
import com.example.plan_it.Admins.ChangeProgramSpecialist
import com.example.plan_it.R
import com.google.firebase.database.FirebaseDatabase

class SpecialistAdapter(var mContext: Context, var mSpecialist:List<String?>, var CompanyLocality:String
                     , var CompanyCategory:String, var CompanyName:String) : RecyclerView.Adapter<SpecialistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.specialist_item_admin, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mSpecialist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name!!.setText(mSpecialist[position])

        var reference= FirebaseDatabase.getInstance().getReference("companies").child(CompanyLocality)
            .child(CompanyCategory).child(CompanyName).child("Specialists").child(mSpecialist[position]!!)

        holder.modifica!!.setOnClickListener {
            var intent=Intent(mContext, ChangeProgramSpecialist::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("company locality",CompanyLocality)
            intent.putExtra("company category",CompanyCategory)
            intent.putExtra("company name",CompanyName)
            intent.putExtra("specialist name",mSpecialist[position])
            mContext.startActivity(intent)

        }

        holder.modificaDisponibilitate!!.setOnClickListener {
            var intent=Intent(mContext, ChangeDisponibilitySpecialist::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("company locality",CompanyLocality)
            intent.putExtra("company category",CompanyCategory)
            intent.putExtra("company name",CompanyName)
            intent.putExtra("specialist name",mSpecialist[position])
            mContext.startActivity(intent)
        }

        holder.stergeSpecialist!!.setOnClickListener{
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
        var modifica: Button?=null
        var stergeSpecialist: Button?=null
        var modificaDisponibilitate: Button?=null

        init {
            name=itemView.findViewById(R.id.Name_specialist_item_admin)
            modifica=itemView.findViewById(R.id.ChangeProgram_specialist_item_admin)
            modificaDisponibilitate=itemView.findViewById(R.id.SchimbaDisponibilitate_specialist_item_admin)
            stergeSpecialist=itemView.findViewById(R.id.Remove_specialist_item_admin)
        }
    }
}