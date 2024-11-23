package com.example.movieratingapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.movieratingapplication.databinding.ActivityFeedBinding
import com.example.movieratingapplication.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import androidx.appcompat.widget.Toolbar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var email = ""
    private var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)




        auth = Firebase.auth

        val activeUser = auth.currentUser
        if (activeUser != null){
            val intent = Intent(this,FeedActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


    fun signIn(view: View){
        email = binding.emailText.text.toString()
        password = binding.password.text.toString()
        if(email.isNotEmpty() && email.isNotBlank() && password.isNotEmpty() && password.isNotBlank()){
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent = Intent(this@MainActivity,FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }


        }

        else{
            Toast.makeText(this,"Email or password cannot be empty",Toast.LENGTH_LONG).show()

        }
    }

    fun signUp(view: View){

        email = binding.emailText.text.toString()
        password = binding.password.text.toString()

        if(email.isNotEmpty() && email.isNotBlank() && password.isNotEmpty() && password.isNotBlank()){
          auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
              val intent = Intent(this@MainActivity,FeedActivity::class.java)
              startActivity(intent)
              finish()
          }.addOnFailureListener {
              Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
          }


        }

        else{
            Toast.makeText(this,"Email or password cannot be empty",Toast.LENGTH_LONG).show()

        }

    }
}