package com.nativecitizens.globesurfer.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nativecitizens.globesurfer.databinding.DisplayTimeZoneListBinding


class TimeZoneListAdapter(private val timeZoneListClickListener: TimeZoneListClickListener)
    : RecyclerView.Adapter<TimeZoneListAdapter.ViewHolder>(){

    var data = listOf<String>()
        @SuppressLint("NotifyDataSetChanged")
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeZone = data[position]
        holder.bind(timeZone, timeZoneListClickListener)
    }


    class ViewHolder private constructor(private val binding: DisplayTimeZoneListBinding)
        : RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = DisplayTimeZoneListBinding.inflate(inflater, parent,
                    false)

                return ViewHolder(binding)
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        fun bind(timezone: String, timeZoneListClickListener: TimeZoneListClickListener) {
            binding.timeZoneLabel.text = timezone

            binding.timeZoneCheckBox.setOnClickListener {
                timeZoneListClickListener.onClick(timezone)
            }
        }
    }
}

//OnClickListener for the RecyclerView
class TimeZoneListClickListener (val clickListener: (timeZone: String) -> Unit){
    fun onClick(timeZone: String) = clickListener(timeZone)
}