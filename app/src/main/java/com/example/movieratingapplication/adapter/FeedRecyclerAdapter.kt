package com.example.movieratingapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieratingapplication.databinding.ActivityFeedBinding
import com.example.movieratingapplication.databinding.RecylerRowBinding
import com.example.movieratingapplication.model.Movie
import com.squareup.picasso.Picasso

class FeedRecyclerAdapter(private val movieList:ArrayList<Movie>): RecyclerView.Adapter<FeedRecyclerAdapter.MovieHolder>() {
    class MovieHolder(val binding: RecylerRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val binding = RecylerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MovieHolder(binding)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.binding.recyclerOverviewText.text = movieList.get(position).overview
        holder.binding.recyclerTitleText.text = movieList.get(position).title
        holder.binding.recyclerDateText.text = movieList.get(position).releaseDate
        Picasso.get().load(movieList.get(position).posterImage).into(holder.binding.recyclerImageView)
    }
}