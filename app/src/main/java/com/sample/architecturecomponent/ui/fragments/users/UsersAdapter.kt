package com.sample.architecturecomponent.ui.fragments.users

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
import com.sample.architecturecomponent.model.User


class UsersAdapter(
    private val appExecutors: AppExecutors,
    private val bindingComponent: DataBindingComponent
) : ListAdapter<User, UserItemViewHolder>(
        AsyncDifferConfig.Builder(DiffCallback())
            .setBackgroundThreadExecutor(appExecutors.diskIO())
            .build()
) {

    private class DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(old: User, aNew: User): Boolean{
            return old.userId == aNew.userId
        }

        override fun areContentsTheSame(old: User, aNew: User): Boolean {
            return old == aNew
        }
    }

    var onClickListener: ((Int, User) -> Unit)? = null

    var onLongClickListener: ((Int, User) -> Boolean)? = null

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
    val onClickListener: ((Int, User) -> Unit)? = null,
    val onLongClickListener: ((Int, User) -> Boolean)? = null
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: User?) {
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



