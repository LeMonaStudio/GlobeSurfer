package com.nativecitizens.globesurfer.adapters

import com.nativecitizens.globesurfer.databinding.DisplayCountryListBinding
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class CountryInitialListAdapter() : RecyclerView.Adapter<CountryInitialListAdapter.ViewHolder>(){

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
        val countryInitial = data[position]
        holder.bind(countryInitial)
    }


    class ViewHolder private constructor(private val binding: DisplayCountryListBinding)
        : RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = DisplayCountryListBinding.inflate(inflater, parent,
                    false)
                return ViewHolder(binding)
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        fun bind(countryInitial: String) {
            binding.countryInitial.text = countryInitial
        }
    }
}
