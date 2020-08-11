package com.example.plan_it

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import com.bumptech.glide.Glide
import com.example.plan_it.Class.User
import com.example.plan_it.Users.Programari
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.HashMap

class MainPage_For_Users : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    private var database: FirebaseDatabase? = null
    private var myRef: DatabaseReference? = null
    private val PICK_IMAGE = 1
    internal var imageUri: Uri? = null

    internal var uploadTask: UploadTask? = null
    internal lateinit var storageReference: StorageReference
    internal lateinit var reference: DatabaseReference


    private var profileImage: ImageView? = null
    private var ChangeNameButton:Button?=null
    private var Nume_user:EditText?=null
    private var Telefon_user:TextView?=null
    private var Mail_user:TextView?=null
    private var Search:Button?=null
    private var Search_value:EditText?=null

    var userS: User =
        User("DEFAULT", "", "", "", "", "",null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page__for__users)
        Nume_user=findViewById<EditText>(R.id.Nume)
        Telefon_user=findViewById<TextView>(R.id.CompanyAdress_activity_inscriere)
        Mail_user=findViewById<TextView>(R.id.Mail_user)
        mAuth = FirebaseAuth.getInstance()
        user = mAuth!!.getCurrentUser()
        database = FirebaseDatabase.getInstance()
        myRef = database!!.getReference("Users")

        storageReference = FirebaseStorage.getInstance().getReference("uploads/")


        //database
        myRef!!.child(user!!.getUid()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user1: User? = dataSnapshot.getValue(User::class.java)
                Nume_user!!.setText(user1!!.Name)
                Telefon_user!!.setText(user1!!.Phone)
                Mail_user!!.setText(user1!!.Email)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        profileImage = findViewById(R.id.profileImage_activity_mainPage_for_admins)


        reference = FirebaseDatabase.getInstance().getReference("Users").child(user!!.uid)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userS = dataSnapshot.getValue<User>(User::class.java!!)!!
                if (userS.imageURL.equals("DEFAULT")) {
                    profileImage!!.setImageResource(R.mipmap.ic_launcher)
                } else {
                    Glide.with(baseContext).load(userS.imageURL).into(profileImage!!)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        //

        addOnClickListener()
    }


    fun addOnClickListener() {
        profileImage = findViewById(R.id.profileImage_activity_mainPage_for_admins)
        profileImage!!.setOnClickListener(View.OnClickListener {
            val gallery = Intent()
            gallery.type = "image/*"
            gallery.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(gallery, "Sellect picture"), PICK_IMAGE)
        })

        val LocalitatiValue = findViewById(R.id.Localitate) as Spinner
        val localitati = ArrayAdapter(
            this@MainPage_For_Users,
            android.R.layout.simple_list_item_1, resources.getStringArray(R.array.Localitati)
        )
        localitati.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        LocalitatiValue.setAdapter(localitati)
        LocalitatiValue.setGravity(View.TEXT_ALIGNMENT_CENTER)

        val ServiciiValue = findViewById(R.id.Categorii_srvicii) as Spinner
        val servicii = ArrayAdapter(
            this@MainPage_For_Users,
            android.R.layout.simple_list_item_1, resources.getStringArray(R.array.Categorii_servicii)
        )
        servicii.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ServiciiValue.setAdapter(servicii)
        ServiciiValue.setGravity(View.TEXT_ALIGNMENT_CENTER)


        ChangeNameButton=findViewById(R.id.ChangeName)
        ChangeNameButton!!.setOnClickListener{
            reference = FirebaseDatabase.getInstance().getReference("Users").child(user!!.uid)
            var map =HashMap<String, Any?>()
            map["Name"]=Nume_user!!.text.toString().trim()
            reference.updateChildren(map)

        }

        Search=findViewById(R.id.Cauta)
        Search_value=findViewById(R.id.Cauta_edit_text)

        Search!!.setOnClickListener {
            var intent=Intent(this@MainPage_For_Users, Programari::class.java)
            intent.putExtra("for_search" , Search_value!!.text.toString().trim())
            intent.putExtra("locality" , LocalitatiValue!!.selectedItem.toString())
            intent.putExtra("category" , ServiciiValue!!.selectedItem.toString())
            startActivity(intent)
        }

    }

    fun uploadImage() {
        val pd = ProgressDialog(this)
        pd.setMessage("Uploading...")
        pd.show()
        if (imageUri != null) {
            val fileReferece = storageReference.child(
                System.currentTimeMillis().toString() + "." + getFileExtension(imageUri!!)
            )
            //uploading the imageUri
            fileReferece.putFile(imageUri!!).addOnSuccessListener {
                fileReferece.downloadUrl.addOnCompleteListener { task ->
                    val mUri = task.result!!.toString()
                    reference =
                        FirebaseDatabase.getInstance().getReference("Users").child(user!!.getUid())
                    val map = HashMap<String, Any>()
                    map["imageURL"] = mUri

                    reference.updateChildren(map)
                    pd.dismiss()
                }
            }.addOnProgressListener { taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                pd.setMessage("Uploaded " + progress.toInt() + "%")
            }
        } else {
            Toast.makeText(baseContext, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }


    fun getFileExtension(uri: Uri): String? {
        val contentResolver = baseContext.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data

            if (uploadTask != null && uploadTask!!.isInProgress()) {
                Toast.makeText(this, "Upload in progress", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage()
            }

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                profileImage!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
