package com.example.plan_it.Admins

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.plan_it.Admins.Adapter.CompanyAdapter
import com.example.plan_it.Admins.Adapter.ServiceAdapter
import com.example.plan_it.Admins.Adapter.SpecialistAdapter
import com.example.plan_it.Class.Company
import com.example.plan_it.Class.Specialist
import com.example.plan_it.Class.User
import com.example.plan_it.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.HashMap

class AdministrateCompany : AppCompatActivity() {
    private var CompanyName:TextView?=null
    private var CompanyAdress:EditText?=null
    private var ChangeCompanyAdress:Button?=null
    private var AddServices:Button?=null
    private var AddSpecialists:Button?=null
    private var ServicesRecyclerView:RecyclerView?=null
    private var SpecialistsRecyclerView:RecyclerView?=null

    private var companyImage: ImageView? = null
    private val PICK_IMAGE = 1
    internal var imageUri: Uri? = null

    internal var uploadTask: UploadTask? = null
    internal lateinit var storageReference: StorageReference
    internal lateinit var referencePhoto: DatabaseReference

    var CompanyLocalityS:String=""
    var CompanyCategoryS:String=""
    var CompanyNameS:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrate_company)

        var intent=getIntent()
        CompanyLocalityS=intent.getStringExtra("company locality")
        CompanyCategoryS=intent.getStringExtra("company category")
        CompanyNameS=intent.getStringExtra("company name")

        CompanyName=findViewById(R.id.Company_Name_change_activity_administrate_company)
        CompanyAdress=findViewById(R.id.company_Adress_change_activity_administrate_company)
        ChangeCompanyAdress=findViewById(R.id.changeName_activity_administrate_company)
        AddServices=findViewById(R.id.addService_activity_administrate_company)
        AddSpecialists=findViewById(R.id.AddSpecialist_activity_administrate_company)
        ServicesRecyclerView=findViewById(R.id.Srevices_ReciclerView_activity_administrate_company)
        ServicesRecyclerView!!.setHasFixedSize(true)
        ServicesRecyclerView!!.setLayoutManager(LinearLayoutManager(baseContext))

        SpecialistsRecyclerView=findViewById(R.id.Specialists_recyclerView_activity_administrate_company)
        SpecialistsRecyclerView!!.setHasFixedSize(true)
        SpecialistsRecyclerView!!.setLayoutManager(LinearLayoutManager(baseContext))


        //database

        var reference1=FirebaseDatabase.getInstance().getReference("companies").child(CompanyLocalityS!!)
            .child(CompanyCategoryS!!)
        var reference=reference1.child(CompanyNameS!!)
        var refuser=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("Companies")


        reference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                    var company: Company=snapshot.getValue(Company::class.java)!!
                CompanyName!!.setText(company.Name)
                CompanyAdress!!.setText(company.Adress)
                var serviceAdapter=
                       ServiceAdapter(baseContext,company.Services.values.toList(),CompanyLocalityS,CompanyCategoryS,CompanyNameS)
                ServicesRecyclerView!!.setAdapter(serviceAdapter)
                var specialistAdapter=
                    SpecialistAdapter(baseContext,company.Specialists.keys.toList(),CompanyLocalityS,CompanyCategoryS,CompanyNameS)
                SpecialistsRecyclerView!!.setAdapter(specialistAdapter)

                if (company!!.imageUrl.equals("Default")) {
                            companyImage!!.setImageResource(R.mipmap.ic_launcher)
                } else {
                      Glide.with(baseContext).load(company!!.imageUrl).into(companyImage!!)
                }
            }
        })

        ChangeCompanyAdress!!.setOnClickListener {
            val map = HashMap<String, Any?>()
            map["Adress"] = CompanyAdress!!.text.trim().toString()
            reference.updateChildren(map)
        }

        //database
        storageReference = FirebaseStorage.getInstance().getReference("uploads/")

        AddServices!!.setOnClickListener {
            var intent= Intent(this@AdministrateCompany,AddService::class.java)
            intent.putExtra("company locality",CompanyLocalityS)
            intent.putExtra("company category",CompanyCategoryS)
            intent.putExtra("company name",CompanyNameS)
            startActivity(intent)
        }

        AddSpecialists!!.setOnClickListener {
            var intent= Intent(this@AdministrateCompany,AddSpecialist::class.java)
            intent.putExtra("company locality",CompanyLocalityS)
            intent.putExtra("company category",CompanyCategoryS)
            intent.putExtra("company name",CompanyNameS)
            startActivity(intent)
        }

        companyImage = findViewById(R.id.CompanyImage_activity_administrate_company)
        companyImage!!.setOnClickListener {
            val gallery = Intent()
            gallery.type = "image/*"
            gallery.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(gallery, "Select picture"), PICK_IMAGE)
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
                    referencePhoto =
                        FirebaseDatabase.getInstance().getReference("companies")
                            .child(CompanyLocalityS).child(CompanyCategoryS).child(CompanyNameS)
                    val map = HashMap<String, Any>()
                    map["imageUrl"] = mUri
                    referencePhoto.updateChildren(map)
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
                companyImage!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
