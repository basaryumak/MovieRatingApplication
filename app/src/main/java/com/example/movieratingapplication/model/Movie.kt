package com.example.movieratingapplication.model
import androidx.lifecycle.MutableLiveData
import java.math.BigDecimal
import java.math.RoundingMode

data class Movie(val overview: String, val posterImage: String, val releaseDate: String, val title: String, val ratings: MutableLiveData<List<Float>>, val iD: String) {


    fun calculateAverageRating(): Float {
        val ratingsList = ratings.value ?: emptyList()
        if (ratingsList.isEmpty()) {
            return 0.0f
        }
        val average = ratingsList.sum() / ratingsList.size
        return BigDecimal(average.toString()).setScale(1, RoundingMode.HALF_UP).toFloat()
    }

    fun getNumberOfRatings(): Int {
        return ratings.value?.size ?: 0
    }
}
