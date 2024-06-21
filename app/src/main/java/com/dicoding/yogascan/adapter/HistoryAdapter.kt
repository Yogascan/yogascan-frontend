package com.dicoding.yogascan.adapter


import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.yogascan.R
import com.dicoding.yogascan.data.response.HistoryItem
import com.dicoding.yogascan.data.response.PosesItem
import com.dicoding.yogascan.databinding.ItemHistoryBinding
import com.dicoding.yogascan.databinding.ItemPoseBinding
import com.dicoding.yogascan.ui.detail.DetailActivity
import com.dicoding.yogascan.ui.history.HistoryFragment
import com.dicoding.yogascan.ui.scan.ScanActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

class HistoryAdapter : ListAdapter<HistoryItem, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = getItem(position)
        holder.bind(historyItem)
    }

    inner class HistoryViewHolder(
        private val binding: ItemHistoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(history: HistoryItem) {
            try {
                val originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
                val dateTime = LocalDateTime.parse(history.date, originalFormatter)
                val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy | hh.mm a", Locale.ENGLISH)
                val formattedDate = dateTime.format(outputFormatter)

                binding.date.text = formattedDate
            } catch (e: DateTimeParseException) {
                println("Error parsing the date string: ${e.message}")
            }
            binding.tvName.text = history.pose.poseName
            Glide.with(binding.root.context)
                .load(history.pose.poseImage) // Menggunakan pose.poseImage dari HistoryItem
                .into(binding.imgPhotos)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryItem>() {
            override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem.result == newItem.result
            }

            override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
