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
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.HashMap

class MainPage_For_Admins : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    private var database: FirebaseDatabase? = null
    private var myRef: DatabaseReference? = null
    private val PICK_IMAGE = 1
    internal var imageUri: Uri? = null
    private val task: Task<Uri>? = null
    internal var uploadTask: UploadTask? = null
    internal lateinit var storageReference: StorageReference
    internal lateinit var reference: DatabaseReference


    private var profileImage: ImageView? = null
    private var hellomessage: TextView? = null
    private var my_info: Button? = null
    private var egibility: Button? = null
    private var programare: Button? = null
    var userS: User = User("DEFAULT", "", "", "", "", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page__for__admins)

        mAuth = FirebaseAuth.getInstance()
        user = mAuth!!.getCurrentUser()
        database = FirebaseDatabase.getInstance()
        myRef = database!!.getReference("Users")

        storageReference = FirebaseStorage.getInstance().getReference("uploads/")

        hellomessage = findViewById(R.id.hellomessage)

        myRef!!.child(user!!.getUid()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user1: User? = dataSnapshot.getValue(User::class.java)
                hellomessage!!.setText("Salut " + user1!!.Name + " !")
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        profileImage = findViewById(R.id.profileImage_user)


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
        addOnClickListener()
    }

    fun addOnClickListener() {
        profileImage = findViewById(R.id.profileImage_user)
        profileImage!!.setOnClickListener(View.OnClickListener {
            val gallery = Intent()
            gallery.type = "image/*"
            gallery.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(gallery, "Sellect picture"), PICK_IMAGE)
        })
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