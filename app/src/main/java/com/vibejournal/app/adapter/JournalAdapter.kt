package com.vibejournal.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vibejournal.app.databinding.ItemJournalBinding
import com.vibejournal.app.model.GoogleDoc
import java.text.SimpleDateFormat
import java.util.*

class JournalAdapter(
    private val onJournalClick: (GoogleDoc) -> Unit
) : ListAdapter<GoogleDoc, JournalAdapter.JournalViewHolder>(JournalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val binding = ItemJournalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JournalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class JournalViewHolder(
        private val binding: ItemJournalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(journal: GoogleDoc) {
            binding.apply {
                tvTitle.text = journal.title
                tvModified.text = formatDate(journal.modifiedTime)
                
                root.setOnClickListener {
                    onJournalClick(journal)
                }
            }
        }

        private fun formatDate(dateString: String?): String {
            return try {
                if (dateString != null) {
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                    val date = inputFormat.parse(dateString)
                    outputFormat.format(date ?: Date())
                } else {
                    "Unknown"
                }
            } catch (e: Exception) {
                "Unknown"
            }
        }
    }

    class JournalDiffCallback : DiffUtil.ItemCallback<GoogleDoc>() {
        override fun areItemsTheSame(oldItem: GoogleDoc, newItem: GoogleDoc): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GoogleDoc, newItem: GoogleDoc): Boolean {
            return oldItem == newItem
        }
    }
}
