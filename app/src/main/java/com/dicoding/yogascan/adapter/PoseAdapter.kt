package com.dicoding.yogascan.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.yogascan.databinding.ItemPoseBinding
import com.dicoding.yogascan.data.response.PosesItem
import com.dicoding.yogascan.ui.detail.DetailActivity
import com.dicoding.yogascan.ui.scan.ScanActivity

class PoseAdapter : ListAdapter<PosesItem, PoseAdapter.PoseViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoseViewHolder {
        val binding = ItemPoseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PoseViewHolder(binding)

    }

    override fun onBindViewHolder(holder: PoseViewHolder, position: Int) {
        val pose = getItem(position)
        holder.bind(pose)
    }

    inner class PoseViewHolder(
        private val binding: ItemPoseBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(poses: PosesItem) {
            binding.tvName.text = poses.poseName
            Glide.with(binding.root.context)
                .load(poses.poseImage)
                .into(binding.imgPhotos)
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.KEY, poses.poseId)
                }
                context.startActivity(intent)
            }
            binding.btnStart.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, ScanActivity::class.java).apply {
                    putExtra(ScanActivity.KEY, poses.poseId)
                    putExtra(ScanActivity.IMAGE, poses.poseImage)
                }
                context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PosesItem>() {
            override fun areItemsTheSame(oldItem: PosesItem, newItem: PosesItem): Boolean {
                return oldItem.poseId == newItem.poseId
            }

            override fun areContentsTheSame(oldItem: PosesItem, newItem: PosesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
