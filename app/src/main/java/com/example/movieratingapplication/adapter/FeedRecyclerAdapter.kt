package com.example.movieratingapplication.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.movieratingapplication.R
import com.example.movieratingapplication.databinding.FilmCardRowBinding
import com.example.movieratingapplication.model.Movie
import com.squareup.picasso.Picasso
import java.lang.Exception

class FeedRecyclerAdapter(private val movieList: ArrayList<Movie>) : RecyclerView.Adapter<FeedRecyclerAdapter.MovieHolder>() {

    class MovieHolder(val binding: FilmCardRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val binding = FilmCardRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieHolder(binding)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movie = movieList[position]
        holder.binding.recyclerTitleText.text = movie.title
        holder.binding.recyclerRatingBar.rating = movie.calculateAverageRating()

        val target = object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: android.graphics.Bitmap, from: Picasso.LoadedFrom) {
                Log.d("FeedRecyclerAdapter", "onBitmapLoaded: Bitmap successfully loaded")
                holder.binding.recyclerImageView.setImageBitmap(bitmap)

                androidx.palette.graphics.Palette.from(bitmap).generate { palette ->
                    Log.d("FeedRecyclerAdapter", "Palette generated")
                    val defaultColor = androidx.core.content.ContextCompat.getColor(
                        holder.binding.root.context,
                        android.R.color.white
                    )
                    val vibrantColor = palette?.getVibrantColor(defaultColor) ?: defaultColor

                    val softenedColor = ColorUtils.blendARGB(vibrantColor, android.graphics.Color.WHITE, 0.85f)
                    val semiTransparentColor = ColorUtils.setAlphaComponent(softenedColor, 255)

                    Log.d("FeedRecyclerAdapter", "Softened & semi-transparent color: $semiTransparentColor")

                    // Set the blended, semi-transparent color
                    val cardView = holder.binding.root
                    cardView.setCardBackgroundColor(semiTransparentColor)
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                Log.e("FeedRecyclerAdapter", "onBitmapFailed: Bitmap failed to load", e)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                Log.d("FeedRecyclerAdapter", "onPrepareLoad: Preparing to load bitmap")
            }
        }

        // Load the image into the target
        Picasso.get().load(movie.posterImage).into(target)

        holder.binding.recyclerRateButton.setOnClickListener {
            val navController = findNavController(holder.itemView)
            val action = R.id.action_movieRecyclerFragment_to_rateFragment
            val currentDestination = navController.currentDestination?.id

            if (currentDestination == R.id.rateFragment) {
                navController.navigate(R.id.action_rateFragment_to_movieRecyclerFragment)
            }

            val bundle = Bundle().apply {
                putString("MOVIE_ID", movie.iD)
            }
            navController.navigate(action, bundle)
        }
    }
}
