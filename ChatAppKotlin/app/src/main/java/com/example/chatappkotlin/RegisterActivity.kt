package com.example.chatappkotlin

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_profileimg.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }

        btn_register.setOnClickListener {
            performRegister()
        }

        btn_registerToLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java);
            startActivity(intent);
        }
    }

    //-----------------------
    var selectedPhotoUri : Uri? = null
    //after btn_profileimg
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Toast.makeText(this,"SELECTED",Toast.LENGTH_SHORT).show()

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            civ_profileimg.setImageBitmap(bitmap)
            btn_profileimg.alpha = 0f
        //            val bitmapDrawable = BitmapDrawable(bitmap)
        //            btn_profileimg.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister(){
        val name = et_username.text.toString()
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please enter email/pw",Toast.LENGTH_SHORT).show()
            return
        }
        //Firebase Auth
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {
                if(!it.isSuccessful) return@addOnCompleteListener
                Log.d("Main","Successfully created user with uid: ${it.result.user?.uid}")

                //---------------------- upload image
                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Toast.makeText(this,"${it.message}",Toast.LENGTH_SHORT).show()
                Log.d("Main","Failed : ${it.message}")
            }
    }

    private  fun uploadImageToFirebaseStorage(){
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/${filename}")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener { Log.d("Register","Successfully uploaded")
            ref.downloadUrl.addOnSuccessListener {
                it.toString()
                Log.d("Register","File Location: $it")

                saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener{ Log.d("Register","Nahi hua upload ${it.message}") }

    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid?:""
        val ref  = FirebaseDatabase.getInstance().getReference("users/${uid}")
        val user = User(uid,et_username.text.toString(),profileImageUrl)
        Log.d("RegisterActivity" , "Reached here")
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity" , "Saved to firebase Database")
                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}

class User(val uid:String,val username:String , val profileImageUrl : String)