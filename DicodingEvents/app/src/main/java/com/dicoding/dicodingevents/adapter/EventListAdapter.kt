package com.dicoding.dicodingevents.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevents.R
import com.dicoding.dicodingevents.data.response.ListEventsItem
import com.dicoding.dicodingevents.databinding.ItemEventBinding
import com.dicoding.dicodingevents.utils.changeFormatDate

class EventListAdapter : ListAdapter<ListEventsItem, EventListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback : OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback : OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(val binding : ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        val context = itemView.context
        fun bind(event : ListEventsItem) {
            binding.apply {
                tvEventName.setText(event.name)
                tvLocationEvent.setText(event.cityName)
                tvTimeEvent.setText(changeFormatDate(event.beginTime))
                tvPatiesEvent.setText(String.format(context.getString(R.string.format_parties), event.registrants, event.quota))
                tvTypeEvent.setText(event.category)
            }
            Glide.with(context)
                .load(event.imageLogo)
                .into(binding.imgEvent)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(event)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data : ListEventsItem)
    }
}