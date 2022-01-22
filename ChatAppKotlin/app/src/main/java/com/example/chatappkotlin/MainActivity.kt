package com.example.chatappkotlin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_register.setOnClickListener {
            performRegister()
        }

        btn_registerToLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java);
            startActivity(intent);
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
            }
            .addOnFailureListener {
                Toast.makeText(this,"${it.message}",Toast.LENGTH_SHORT).show()
                Log.d("Main","Failed : ${it.message}")
            }
    }
}