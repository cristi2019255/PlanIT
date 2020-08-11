package com.example.plan_it

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.android.material.snackbar.Snackbar

import java.util.HashMap
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.example.plan_it.Class.User


class MainActivity : AppCompatActivity() {

    private lateinit var mAuth:FirebaseAuth
    private lateinit var database:FirebaseDatabase
    private lateinit var myRef:DatabaseReference
    private lateinit var Inregistrare:Button
    private lateinit var Autorizare:Button
    lateinit var root:RelativeLayout
    private lateinit var activity_main:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database= FirebaseDatabase.getInstance()
        mAuth=FirebaseAuth.getInstance()
        myRef=database.getReference("users")

        activity_main=findViewById(R.id.root_element)
        root=findViewById(R.id.root_element)
        addOnActionListener()
    }

    fun addOnActionListener(){
        Inregistrare=findViewById(R.id.buttonSignIn) as Button
        Autorizare=findViewById(R.id.buttonRegister) as Button
        Inregistrare.setOnClickListener {
            ShowSignInWindow()
        }
        Autorizare.setOnClickListener {
               ShowAuthorizationWindow()
        }
    }

    private fun ShowAuthorizationWindow() {
        val dialog = AlertDialog.Builder(this@MainActivity)
        dialog.setTitle("Înregistrare")
        dialog.setMessage("Completați toate câmpurile pentru a vă putea Înregistra")
        val inflater = LayoutInflater.from(this@MainActivity)
        val signinview = inflater.inflate(R.layout.inregistrare, null)
        dialog.setView(signinview)

        dialog.setNegativeButton(
            "Termină"
        ) { dialog, which -> dialog.dismiss() }


        val email = signinview.findViewById<EditText>(R.id.emailfield)
        val pass = signinview.findViewById<EditText>(R.id.passwordfield)
        val phone = signinview.findViewById<EditText>(R.id.phonefield)
        val name = signinview.findViewById<EditText>(R.id.namefield)

        var Auth_permited:Boolean = false

        dialog.setPositiveButton("Înregistrare", object:DialogInterface.OnClickListener {

            override fun onClick(dialog:DialogInterface, which:Int) {

                        if (TextUtils.isEmpty(email.text)) {
                            Snackbar.make(root, "nu ați introdus email", Snackbar.LENGTH_SHORT).show()
                            return
                        }
                        if (pass.text.length < 5) {
                            Snackbar.make(root, "parola e prea scurtă", Snackbar.LENGTH_SHORT).show()
                            return
                        }
                        if (phone.text.length != 10) {
                            Snackbar.make(root, "numărul de telefon are 10 cifre", Snackbar.LENGTH_SHORT).show()
                            return
                        }
                        if (TextUtils.isEmpty(name.text)) {
                            Snackbar.make(root, "Numele e invalid", Snackbar.LENGTH_SHORT).show()
                            return
                        }
                        Auth_permited = true

                        val namedb = name.text.toString()
                        val phonedb = phone.text.toString()
                        val emaildb = email.text.toString()
                        val passdb = pass.text.toString()
                        val statusUser:RadioButton = signinview.findViewById(R.id.UserRegister)
                        val statusAdmin:RadioButton = signinview.findViewById(R.id.AdminRegister)


                        mAuth.createUserWithEmailAndPassword(emaildb, passdb)
                            .addOnCompleteListener(this@MainActivity) { task ->
                                if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val user = mAuth.currentUser
                                if (user != null) {
                                    val profileUpdates = UserProfileChangeRequest.Builder()
                                        .setDisplayName(namedb)
                                        .build()
                                    user!!.updateProfile(profileUpdates)
                                // Check if user's email is verified
                                val emailVerified = user!!.isEmailVerified
                                if (!emailVerified) {
                                    user!!.sendEmailVerification().addOnCompleteListener { Toast.makeText(this@MainActivity, "Email-ul de verificare a fost trimis", Toast.LENGTH_SHORT).show() }
                                } else {
                                    Toast.makeText(this@MainActivity, "Email-ul a fost verificat", Toast.LENGTH_SHORT).show()
                                }
                                // The user's ID, unique to the Firebase project. Do NOT use this value to
                                // authenticate with your backend server, if you have one. Use
                                // FirebaseUser.getIdToken() instead.
                                println("User Cristi "+user!!.uid);
                                val reference:DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user!!.uid)
                                val map = HashMap<String, Any?>()
                                map["imageURL"] = "DEFAULT"
                                map["Phone"] = phonedb
                                map["Name"] = namedb
                                map["ID"] = user!!.uid
                                map["Email"] = user!!.email
                                when{
                                    statusUser!!.isChecked-> map["Status"]="user"
                                    statusAdmin!!.isChecked-> map["Status"]="admin"
                                    else->{
                                        Toast.makeText(this@MainActivity,"Te rog sa alegi statutul de utilizare",Toast.LENGTH_SHORT).show()
                                        return@addOnCompleteListener
                                    }
                                }

                                reference.updateChildren(map)

                            }

                            Toast.makeText(this@MainActivity, "Înregistrare cu succes.", Toast.LENGTH_SHORT).show()
                            Toast.makeText(this@MainActivity, "Asteptați emailul cu linkul de înregistrare",Toast.LENGTH_LONG).show()
                        } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(this@MainActivity, "Înregistrare eșuată.Acest email este deja folosit.", Toast.LENGTH_SHORT).show()
                        }
            }
        }
    })

        dialog.show()
    }

    fun ShowSignInWindow(){
           val dialog = AlertDialog.Builder(this)
           dialog.setTitle("Autentificare")
            dialog.setMessage("Completați câmpurile")
            val inflater = LayoutInflater.from(this)
            val authorization = inflater.inflate(R.layout.autorizare, null)
            dialog.setView(authorization)

            val email = authorization.findViewById<EditText>(R.id.emailFieldA)
            val pass = authorization.findViewById<EditText>(R.id.passwordFieldA)
            val statusUser = authorization.findViewById<RadioButton>(R.id.User)
            val statusAdmin = authorization.findViewById<RadioButton>(R.id.Admin)

            dialog.setNegativeButton("Termină") { dialog, which -> dialog.dismiss() }

            dialog.setPositiveButton("Autentificare", object:DialogInterface.OnClickListener {

                override fun onClick(dialog:DialogInterface, which:Int) {
                    if (TextUtils.isEmpty(email.getText().toString())) {
                        Snackbar.make(root, "Vă rugăm să introduceți email-ul", Snackbar.LENGTH_SHORT).show()
                        return
                    }
                    if (pass.getText().length < 5) {
                        Snackbar.make(root, "Parola prea scurtă", Snackbar.LENGTH_SHORT).show()
                        return
                    }
                    val emaildb = email.getText().toString()
                    val passdb = pass.getText().toString()

                    mAuth.signInWithEmailAndPassword(emaildb, passdb).addOnCompleteListener(this@MainActivity
                    ) { task ->
                        if (task.isSuccessful) {
                            val user = FirebaseAuth.getInstance().currentUser
                            if (user!!.isEmailVerified()) {

                                    Toast.makeText(this@MainActivity, "Autorizare cu succes", Toast.LENGTH_SHORT).show()
                                    val reference:DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user!!.uid)

                                    reference.addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val user: User? = dataSnapshot.getValue(
                                            User::class.java)
                                            when{
                                                (user!!.Status.equals("user") && (statusUser.isChecked))->
                                                    startActivity(Intent(this@MainActivity, MainPage_For_Users::class.java))
                                                (user!!.Status.equals("admin")&& (statusAdmin.isChecked))->
                                                    startActivity(Intent(this@MainActivity, MainPage_For_Admins::class.java))
                                                else->{
                                                    Toast.makeText(this@MainActivity, "Eroare la autentificare", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                        }
                                    })

                             } else {
                                Toast.makeText(baseContext, "Vă rugăm să verificați email-ul unde urmează să vă apară linkul de înregistrare", Toast.LENGTH_SHORT).show()
                             }
                        } else {
                            Toast.makeText(this@MainActivity, "Autorizare eșuată!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
             })

        dialog.show()
    }

}