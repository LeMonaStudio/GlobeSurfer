package com.nativecitizens.globesurfer.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nativecitizens.globesurfer.R
import com.nativecitizens.globesurfer.databinding.DisplayCountryViewBinding
import com.nativecitizens.globesurfer.model.Country


class CountryListAdapter (private val clickListener: CountryListClickListener)
    : RecyclerView.Adapter<CountryListAdapter.ViewHolder>(){

    var data = listOf<Country>()
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
        val ctx = holder.itemView.context
        val country = data[position]
        holder.bind(country, clickListener,ctx)
    }

    /**
     * ViewHolder class
     */
    class ViewHolder private constructor(private val binding: DisplayCountryViewBinding)
        : RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = DisplayCountryViewBinding.inflate(inflater, parent, false)
                return ViewHolder(binding)
            }
        }


        @SuppressLint("ClickableViewAccessibility")
        fun bind(
            country: Country,
            clickListener: CountryListClickListener,
            ctx: Context
        ) {
            if (!country.flagUrl.isNullOrEmpty()){
                val uri = Uri.parse(country.flagUrl)
                Glide.with(ctx)
                    .load(uri)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_image_loading_error))
                    .into(binding.countryFlag)
            }

            binding.countryName.text = country.name
            binding.countryCapital.text = country.capital

            //OnClick
            binding.countryViewContainer.setOnClickListener {
                clickListener.onClick(country)
            }
        }
    }
}

//OnClickListener for the RecyclerView
class CountryListClickListener (val clickListener: (country: Country) -> Unit){
    fun onClick(country: Country) = clickListener(country)
}