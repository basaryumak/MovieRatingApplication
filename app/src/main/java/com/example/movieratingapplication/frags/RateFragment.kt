package com.example.movieratingapplication.frags

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.movieratingapplication.R
import com.example.movieratingapplication.databinding.FragmentRateBinding
import com.example.movieratingapplication.viewModel.RatesViewModel

class RateFragment : Fragment(R.layout.fragment_rate) {
    private var _binding: FragmentRateBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RatesViewModel
    private var movieID: String? = null

    companion object;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout and initialize binding
        _binding = FragmentRateBinding.inflate(inflater, container, false)
        movieID = arguments?.getString("MOVIE_ID") ?: throw IllegalArgumentException("Movie ID cannot be null")

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[RatesViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.movieName.observe(viewLifecycleOwner) { name ->
            binding.movieNameLabel.text = name ?: "Movie Name Not Available"
        }

        viewModel.userRating.observe(viewLifecycleOwner) { rating ->
            binding.userRatingBar.rating = rating ?: 0f // Default to 0 if no rating is found
        }

        viewModel.fetchMovieName(movieID)
        viewModel.fetchUserRating(movieID)
        viewModel.fetchRates(movieID)

        // Observe current rating data
        viewModel.currentRating.observe(viewLifecycleOwner) { rating ->
            if (rating != null) {
                binding.currentRatingBar.rating = rating.averageRating
                binding.currentNumericRating.text = "${rating.averageRating}/5.0"
                binding.ratingCount.text = "${rating.numberOfRatings} ratings"
            }
        }

        // Handle submit rating button click
        binding.submitRatingButton.setOnClickListener {
            val userRating = binding.userRatingBar.rating
            if (userRating > 0) {
                viewModel.submitUserRating(userRating,movieID)
                Toast.makeText(context, "Rating submitted: $userRating", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Please select a rating.", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle delete rating button click
        binding.deleteRatingButton.setOnClickListener {
            viewModel.deleteUserRating(movieID)
            Toast.makeText(context, "Your rating has been deleted.", Toast.LENGTH_SHORT).show()
            binding.userRatingBar.rating = 0.0f // Reset user rating bar
        }

        // Observe error messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}
