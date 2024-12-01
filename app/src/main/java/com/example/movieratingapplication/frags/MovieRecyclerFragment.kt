package com.example.movieratingapplication.frags

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
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

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        movieArrayList = ArrayList()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        feedAdapter = FeedRecyclerAdapter(movieArrayList)
        binding.recyclerView.adapter = feedAdapter

        receiveData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun receiveData() {
        db.collection("movies").orderBy("release_date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                if (value != null && !value.isEmpty) {
                    val movies = value.documents
                    movieArrayList.clear()
                    for (movie in movies) {
                        val title = movie.getString("title") ?: ""
                        val releaseDate = movie.getString("release_date") ?: ""
                        val overview = movie.getString("overview") ?: ""
                        val posterImage = movie.getString("poster_image") ?: ""
                        val movieId = movie.id

                        val ratingsListLiveData = MutableLiveData<List<Float>>(emptyList())

                        val movieObj = Movie(overview, posterImage, releaseDate, title, ratingsListLiveData, movieId)
                        movieArrayList.add(movieObj)

                        ratingsListLiveData.observe(viewLifecycleOwner) {
                            feedAdapter.notifyDataSetChanged()
                        }

                        movie.reference.collection("ratings").addSnapshotListener { ratingsSnapshot, ratingsError ->
                            if (ratingsError != null) {
                                Toast.makeText(requireContext(), ratingsError.localizedMessage, Toast.LENGTH_LONG).show()
                                return@addSnapshotListener
                            }

                            val ratings = ratingsSnapshot?.documents?.mapNotNull { doc ->
                                doc.getDouble("rate")?.toFloat()
                            } ?: emptyList()

                            ratingsListLiveData.postValue(ratings)
                        }
                    }
                    feedAdapter.notifyDataSetChanged()
                }
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
