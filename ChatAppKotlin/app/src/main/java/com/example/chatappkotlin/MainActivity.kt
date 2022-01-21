package com.example.chatappkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_registerToLogin.setOnClickListener {
            val name = et_username.text.toString()
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            val intent = Intent(this,LoginActivity::class.java);
            startActivity(intent);
        }
    }
}