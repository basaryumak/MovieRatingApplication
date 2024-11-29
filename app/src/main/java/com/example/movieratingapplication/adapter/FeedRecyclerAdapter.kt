package com.example.movieratingapplication.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.movieratingapplication.R
import com.example.movieratingapplication.databinding.RecyclerRowBinding
import com.example.movieratingapplication.frags.RateFragment
import com.example.movieratingapplication.model.Movie
import com.squareup.picasso.Picasso

class FeedRecyclerAdapter(private val movieList:ArrayList<Movie>): RecyclerView.Adapter<FeedRecyclerAdapter.MovieHolder>() {
    class MovieHolder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieHolder(binding)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.binding.recyclerOverviewText.text = movieList.get(position).overview
        holder.binding.recyclerTitleText.text = movieList.get(position).title
        holder.binding.recyclerDateText.text = movieList.get(position).releaseDate
        holder.binding.recyclerRatingBar.rating = movieList.get(position).calculateAverageRating()
        holder.binding.recyclerNumberOfRatingsText.text = movieList.get(position).getNumberOfRatings().toString()
        holder.binding.recyclerNumericRatingText.text = movieList.get(position).calculateAverageRating().toString()
        Picasso.get().load(movieList[position].posterImage).into(holder.binding.recyclerImageView)

        holder.binding.recyclerRateButton.setOnClickListener {
            val navController = findNavController(holder.itemView)
            val action = R.id.action_movieRecyclerFragment_to_rateFragment // Replace with your actual action ID
            val currentDestination = findNavController(holder.itemView).currentDestination?.id
            if (currentDestination == R.id.rateFragment) {
                findNavController(holder.itemView).navigate(R.id.action_rateFragment_to_movieRecyclerFragment)
            }

            val bundle = Bundle().apply {
                putString("MOVIE_ID", movieList[position].ID) // Pass the movie ID
            }
            navController.navigate(action, bundle)   }
    }
}