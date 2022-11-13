package com.nativecitizens.globesurfer.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nativecitizens.globesurfer.databinding.DisplayContinentListBinding


class ContinentListAdapter(private val continentListClickListener: ContinentListClickListener)
    : RecyclerView.Adapter<ContinentListAdapter.ViewHolder>(){

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
        val continent = data[position]
        holder.bind(continent, continentListClickListener)
    }


    class ViewHolder private constructor(private val binding: DisplayContinentListBinding)
        : RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = DisplayContinentListBinding.inflate(inflater, parent,
                    false)

                return ViewHolder(binding)
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        fun bind(continent: String, continentListClickListener: ContinentListClickListener) {
            binding.continent.text = continent

            binding.continentCheckBox.setOnClickListener {
                continentListClickListener.onClick(continent)
            }
        }
    }
}


//OnClickListener for the RecyclerView
class ContinentListClickListener (val clickListener: (continent: String) -> Unit){
    fun onClick(continent: String) = clickListener(continent)
}