package com.example.movieratingapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movieratingapplication.databinding.CommentRowBinding
import com.example.movieratingapplication.viewModel.Comment

class CommentListAdapter(
    private val currentUserId: String?,
    private val onDeleteClick: (String) -> Unit // Callback for delete action
) : ListAdapter<Comment, CommentListAdapter.CommentViewHolder>(CommentDiffCallback()) {

    // ViewHolder class for binding the comment row
    class CommentViewHolder(val binding: CommentRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CommentRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)

        // Set comment text and timestamp
        holder.binding.commentTextView.text = comment.commentText
        holder.binding.timestampTextView.text = android.text.format.DateFormat.format(
            "MMM dd, yyyy h:mm a",
            comment.timestamp
        ).toString()

        // Show delete button if the comment belongs to the current user
        if (comment.userId == currentUserId) {
            holder.binding.deleteCommentButton.apply {
                visibility = android.view.View.VISIBLE
                setOnClickListener {
                    onDeleteClick(comment.commentId) // Call the delete callback with the comment ID
                }
            }
        } else {
            holder.binding.deleteCommentButton.visibility = android.view.View.GONE
        }
    }

    // DiffUtil for efficient updates
    class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.commentId == newItem.commentId
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }
}