package com.example.movieratingapplication.frags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieratingapplication.adapter.CommentListAdapter
import com.example.movieratingapplication.databinding.FragmentCommentBinding
import com.example.movieratingapplication.viewModel.CommentViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class CommentFragment : Fragment() {

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!

    private val commentsViewModel: CommentViewModel by viewModels()
    private lateinit var commentListAdapter: CommentListAdapter

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    private var movieId: String? = null // Pass this as an argument to the fragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieId = arguments?.getString("MOVIE_ID")
        if (movieId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Movie ID is missing.", Toast.LENGTH_LONG).show()
            return
        }

        setupRecyclerView()
        observeViewModel()

        binding.submitCommentButton.setOnClickListener {
            submitComment()
        }

        commentsViewModel.fetchComments(movieId)
    }

    private fun setupRecyclerView() {
        commentListAdapter = CommentListAdapter(currentUserId) { commentId ->
            deleteComment(commentId)
        }

        binding.commentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = commentListAdapter
        }
    }

    private fun observeViewModel() {
        commentsViewModel.comments.observe(viewLifecycleOwner) { comments ->
            commentListAdapter.submitList(comments)
        }

        commentsViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun submitComment() {
        val commentText = binding.commentEditText.text.toString().trim()
        if (commentText.isEmpty()) {
            Toast.makeText(requireContext(), "Comment cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            commentsViewModel.submitComment(movieId, commentText)
            binding.commentEditText.text?.clear()
        }
    }

    private fun deleteComment(commentId: String) {
        lifecycleScope.launch {
            commentsViewModel.deleteComment(movieId, commentId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}