package com.example.movieratingapplication.model

data class Movie(val overview:String,val posterImage:String, val releaseDate:String,val title:String, val ratings:Map<String, Float>) {
    fun getAverageRating(): Float {
        return if (ratings.isNotEmpty()) {
            ratings.values.sum() / ratings.size
        } else {
            0f
        }
    }

    fun getNumberOfRatings(): Int {
        return ratings.size
    }
}