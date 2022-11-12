package com.nativecitizens.globesurfer.adapters

import com.nativecitizens.globesurfer.databinding.DisplayCountryListBinding
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nativecitizens.globesurfer.R
import com.nativecitizens.globesurfer.model.Country


class CountryInitialListAdapter(private val clickListener: CountryListClickListener)
    : RecyclerView.Adapter<CountryInitialListAdapter.ViewHolder>(){

    var data = listOf<Map<String, List<Country>>>()
        @SuppressLint("NotifyDataSetChanged")
        set(value){
            field = value
            notifyDataSetChanged()
        }

    private lateinit var countryListChildRv: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val countryInitialAndList = data[position]
        countryListChildRv = holder.itemView.findViewById(R.id.country_list_child)

        holder.bind(holder.itemView.context, countryInitialAndList, countryListChildRv, clickListener)
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
        fun bind(
            ctx: Context,
            countryInitialAndList: Map<String, List<Country>>,
            countryListChildRv: RecyclerView,
            clickListener: CountryListClickListener
        ) {
            val countryInitial = countryInitialAndList.keys.elementAt(0)
            binding.countryInitial.text = countryInitial

            val adapterCountryViewList = CountryListAdapter(CountryListClickListener{
                clickListener.onClick(country = it)
            })
            val layoutManagerCountryViewList = LinearLayoutManager(ctx, RecyclerView.VERTICAL, false)

            countryListChildRv.apply {
                adapter = adapterCountryViewList
                layoutManager = layoutManagerCountryViewList
            }
            countryInitialAndList[countryInitial]?.let {
                adapterCountryViewList.data = it
            }
        }
    }
}
