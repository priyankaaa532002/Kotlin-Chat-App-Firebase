package com.example.chatappkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            val email = et_login_email.text.toString()
            val password = et_login_password.text.toString()
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Please enter email/pw",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show()
                    else Toast.makeText(this,"Not successful",Toast.LENGTH_SHORT).show()
                }
        }

        btn_forgotpass.setOnClickListener {
            val email = et_login_email.text.toString()
            if (email.isEmpty()){
                Toast.makeText(this,"Please enter a valid email address",Toast.LENGTH_SHORT).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this,"Check your registered email\nto change your password.",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this,"${it.exception}",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}