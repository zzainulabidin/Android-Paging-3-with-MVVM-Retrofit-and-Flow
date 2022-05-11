package dev.ronnie.allplayers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.ronnie.allplayers.R
import dev.ronnie.allplayers.databinding.AdapterUserItemBinding
import dev.ronnie.allplayers.models.User

class UsersAdapter(private val clicked: (String) -> Unit) :
    PagingDataAdapter<User, UsersAdapter.UsersViewHolder>(
        UsersDiffCallback()
    ) {

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
            AdapterUserItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    inner class UsersViewHolder(
        private val binding: AdapterUserItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: User?) {

            binding.let { binding ->

                binding.name.text = data?.login
                data?.avatarUrl.let {
                    Glide.with(this.itemView.context)
                        .load(data?.avatarUrl)
                        .placeholder(R.drawable.user_icon_placeholder)
                        .into(binding.profileImage)
                }

                binding.root.setOnClickListener {
                    clicked.invoke("")
                }
            }

        }
    }

    private class UsersDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

}