package com.example.movieratingapplication.model
import java.math.BigDecimal
import java.math.RoundingMode

data class Movie(val overview:String,val posterImage:String, val releaseDate:String,val title:String, val ratings: List<Float>, val iD: String) {



    fun calculateAverageRating(): Float {
        val average = this.ratings.sum() / this.ratings.size.toFloat()
        return BigDecimal(average.toString()).setScale(1, RoundingMode.HALF_UP).toFloat()
    }


    // Helper function to get the number of ratings for a specific movie
    fun getNumberOfRatings(): Int {
        return this.ratings.size
    }
}
