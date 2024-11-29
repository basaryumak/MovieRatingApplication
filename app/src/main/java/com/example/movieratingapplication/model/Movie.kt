package com.example.movieratingapplication.model

data class Movie(val overview:String,val posterImage:String, val releaseDate:String,val title:String, val ratings: List<Float>, val ID: String) {

    fun calculateAverageRating(): Float {
        return (this.ratings.sum()/this.ratings.size).toFloat()
    }

    // Helper function to get the number of ratings for a specific movie
    fun getNumberOfRatings(): Int {
        return this.ratings.size
    }
}
