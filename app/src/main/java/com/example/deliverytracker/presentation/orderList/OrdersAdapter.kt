package com.example.deliverytracker.presentation.orderList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deliverytracker.data.model.Order
import com.example.deliverytracker.data.model.OrderEntity
import com.example.deliverytracker.databinding.RowAddressBinding

class OrdersAdapter(val context: Context, val listener: (model: Order?) -> Unit): ListAdapter<Order, OrdersAdapter.OrderViewHolder>(DiffCallback()) {

    private var list: List<OrderEntity>? = null

    inner class OrderViewHolder(private val binding: RowAddressBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Order?) {
            binding.tvName.text = item?.customerName
            binding.tvOrderNo.text = item?.orderNumber
            binding.tvAddress.text = item?.address
            binding.root.setOnClickListener{
                listener.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            RowAddressBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(holder.adapterPosition))
    }

    class DiffCallback: DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            TODO("Not yet implemented")
        }

    }

}