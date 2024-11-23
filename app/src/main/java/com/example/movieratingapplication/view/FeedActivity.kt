package com.example.movieratingapplication.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieratingapplication.R
import com.example.movieratingapplication.adapter.FeedRecyclerAdapter
import com.example.movieratingapplication.databinding.ActivityFeedBinding
import com.example.movieratingapplication.model.Movie
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class FeedActivity : AppCompatActivity() {


    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var movieArrayList: ArrayList<Movie>
    private lateinit var feedAdapter: FeedRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        db = Firebase.firestore
        movieArrayList = ArrayList<Movie>()
        receiveData()
        binding.recylerView.layoutManager = LinearLayoutManager(this)
        feedAdapter = FeedRecyclerAdapter(movieArrayList)
        binding.recylerView.adapter = feedAdapter
    }


    private fun receiveData(){
        db.collection("movies").orderBy("release_date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if(error != null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }

            else{
                if (value!=null && !value.isEmpty){
                    val movies = value.documents
                    movieArrayList.clear()
                    for(movie in movies){
                        val title = movie.get("title") as String
                        val releaseDate = movie.get("release_date") as String
                        val overview = movie.get("overview") as String
                        val posterImage = movie.get("poster_image") as String
                        val movie = Movie(overview,posterImage,releaseDate,title)
                        movieArrayList.add(movie)
                    }

                    feedAdapter.notifyDataSetChanged()

                }

            }



        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.rate){
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        else if (item.itemId == R.id.signout){
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }




}