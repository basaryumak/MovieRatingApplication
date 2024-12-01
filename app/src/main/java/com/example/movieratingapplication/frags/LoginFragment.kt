package com.example.movieratingapplication.frags

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.movieratingapplication.R
import com.example.movieratingapplication.databinding.FragmentLoginBinding
import com.example.movieratingapplication.viewModel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            authViewModel.isAuthenticated.value = true
            navigateToMovies(currentUser.email)
        }

        binding.signInButton.setOnClickListener { signIn() }
        binding.signUpButton.setOnClickListener { signUp() }
    }

    private fun signIn() {
        val email = binding.emailText.text.toString().trim()
        val password = binding.password.text.toString().trim()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(requireContext(), "Email or password cannot be empty", Toast.LENGTH_LONG).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user != null) {
                    // Update ViewModel's state
                    authViewModel.isAuthenticated.value = true
                    navigateToMovies(user.email)
                }
            }
            .addOnFailureListener { error ->
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun signUp() {
        val email = binding.emailText.text.toString().trim()
        val password = binding.password.text.toString().trim()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(requireContext(), "Email or password cannot be empty", Toast.LENGTH_LONG).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                if (user != null) {
                    authViewModel.isAuthenticated.value = true
                    navigateToMovies(user.email)
                }
            }
            .addOnFailureListener { error ->
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
            }
    }

    private fun navigateToMovies(email: String?) {
        Toast.makeText(requireContext(), "Welcome, $email", Toast.LENGTH_SHORT).show()
        Log.d("DEBUG_USER_EMAIL", "User Email: $email")
        findNavController().navigate(R.id.action_loginFragment_to_movieRecyclerFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
