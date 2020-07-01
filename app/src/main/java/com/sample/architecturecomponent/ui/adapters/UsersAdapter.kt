package com.sample.architecturecomponent.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.architecturecomponent.AppExecutors
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.databinding.ItemListUserBinding
import com.sample.architecturecomponent.vo.UserItem


class UsersAdapter : ListAdapter<UserItem, UserItemViewHolder> {

    constructor(appExecutors: AppExecutors): super(AsyncDifferConfig.Builder(object : DiffUtil.ItemCallback<UserItem>() {
        override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.login == newItem.id &&
                    oldItem.avatar_url == newItem.avatar_url
        }
    }).setBackgroundThreadExecutor(appExecutors.diskIO()).build())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        val binding = DataBindingUtil.inflate<ItemListUserBinding>(LayoutInflater.from(parent.context), R.layout.item_list_user, parent, false)

        return UserItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class UserItemViewHolder(val binding: ItemListUserBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UserItem?) {
        item?.let {
            binding.userItem = it
        }
    }
}