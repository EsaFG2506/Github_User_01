package com.dicoding.githubuser.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.data.response.UserItem
import com.dicoding.githubuser.databinding.ItemUserBinding

class UserAdapter : ListAdapter<UserItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback (onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class MyViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserItem){

            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(user)
            }

            binding.tvUsername.text = user.login
            binding.tvUrl.text = user.htmlUrl
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .into(binding.ivUser)
        }
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: UserItem)
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserItem>() {

            override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}

