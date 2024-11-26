package com.example.movieratingapplication.frags

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieratingapplication.R
import com.example.movieratingapplication.adapter.FeedRecyclerAdapter
import com.example.movieratingapplication.databinding.FragmentMovieRecyclerBinding
import com.example.movieratingapplication.model.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MovieRecyclerFragment : Fragment(R.layout.fragment_movie_recycler) {

    private var _binding: FragmentMovieRecyclerBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var movieArrayList: ArrayList<Movie>
    private lateinit var feedAdapter: FeedRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase and RecyclerView
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        movieArrayList = ArrayList()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        feedAdapter = FeedRecyclerAdapter(movieArrayList)
        binding.recyclerView.adapter = feedAdapter

        // Load data
        receiveData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun receiveData() {
        db.collection("movies").orderBy("release_date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
                } else {
                    if (value != null && !value.isEmpty) {
                        val movies = value.documents
                        movieArrayList.clear()
                        for (movie in movies) {
                            val title = movie.get("title") as String
                            val releaseDate = movie.get("release_date") as String
                            val overview = movie.get("overview") as String
                            val posterImage = movie.get("poster_image") as String
                            val ratings = (movie.get("ratings") as? Map<*, *>)?.mapNotNull { entry ->
                                val key = entry.key as? String       // Ensure the key is a String
                                val value = (entry.value as? Number)?.toFloat() // Ensure the value is a Number, then convert to Float
                                if (key != null && value != null) key to value else null
                                 }?.toMap() ?: emptyMap()

// Now create the Movie object with the parsed ratings

                            val movieObj = Movie(overview, posterImage, releaseDate, title, ratings)
                            movieArrayList.add(movieObj)
                        }
                        feedAdapter.notifyDataSetChanged()
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
