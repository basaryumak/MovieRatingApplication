package com.example.movieratingapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.movieratingapplication.R
import com.example.movieratingapplication.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Check if user is already authenticated
        val currentUser = auth.currentUser


        // Set up button click listeners
        binding.signInButton.setOnClickListener { signIn() }
        binding.signUpButton.setOnClickListener { signUp() }
    }

    private fun signIn() {
        val email = binding.emailText.text.toString().trim()
        val password = binding.password.text.toString().trim()

        // Validate inputs
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(requireContext(), "Email or password cannot be empty", Toast.LENGTH_LONG).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Navigate to MovieRecyclerFragment on successful login
                findNavController().navigate(R.id.action_loginFragment_to_movieRecyclerFragment)
            }
            .addOnFailureListener { error ->
                // Show error message
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun signUp() {
        val email = binding.emailText.text.toString().trim()
        val password = binding.password.text.toString().trim()

        // Validate inputs
        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(requireContext(), "Email or password cannot be empty", Toast.LENGTH_LONG).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Navigate to MovieRecyclerFragment on successful sign-up
                findNavController().navigate(R.id.action_loginFragment_to_movieRecyclerFragment)
            }
            .addOnFailureListener { error ->
                // Show error message
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
