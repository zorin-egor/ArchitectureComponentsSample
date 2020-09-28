package com.sample.architecturecomponent.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.architecturecomponent.AppExecutors
import com.sample.architecturecomponent.R
import com.sample.architecturecomponent.databinding.ItemListUserBinding
import com.sample.architecturecomponent.vo.UserItem


class UsersAdapter(
    private val appExecutors: AppExecutors,
    private val bindingComponent: DataBindingComponent
) : ListAdapter<UserItem, UserItemViewHolder>(
        AsyncDifferConfig.Builder(DiffCallback())
            .setBackgroundThreadExecutor(appExecutors.diskIO())
            .build()
) {

    private class DiffCallback : DiffUtil.ItemCallback<UserItem>() {
        override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean{
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
            return oldItem == newItem
        }
    }

    var onClickListener: ((Int, UserItem) -> Unit)? = null

    var onLongClickListener: ((Int, UserItem) -> Boolean)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {
        return UserItemViewHolder(
            DataBindingUtil.inflate<ItemListUserBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_list_user,
                parent,
                false,
                bindingComponent
            ),
            onClickListener,
            onLongClickListener
        )
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class UserItemViewHolder(
    val binding: ItemListUserBinding,
    val onClickListener: ((Int, UserItem) -> Unit)? = null,
    val onLongClickListener: ((Int, UserItem) -> Boolean)? = null
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UserItem?) {
        item?.let {
            binding.apply {
                userItem = it
                userLayout.setOnClickListener {
                    onClickListener?.invoke(layoutPosition, item)
                }
                userLayout.setOnLongClickListener {
                    onLongClickListener?.invoke(layoutPosition, item) ?: false
                }
            }
        }
    }
}



