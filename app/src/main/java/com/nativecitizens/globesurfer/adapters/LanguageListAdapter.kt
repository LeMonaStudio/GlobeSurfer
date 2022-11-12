package com.nativecitizens.globesurfer.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nativecitizens.globesurfer.databinding.DisplayLanguageListBinding


class LanguageListAdapter(private val clickListener: LanguageListClickListener)
    : RecyclerView.Adapter<LanguageListAdapter.ViewHolder>(){

    var data = listOf<Map<String, Boolean>>()
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
        val languageCodeSelectionMap = data[position]
        holder.bind(languageCodeSelectionMap, clickListener, position)
    }


    class ViewHolder private constructor(private val binding: DisplayLanguageListBinding)
        : RecyclerView.ViewHolder(binding.root){

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = DisplayLanguageListBinding.inflate(inflater, parent,
                    false)
                return ViewHolder(binding)
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        fun bind(
            languageCodeSelectionMap: Map<String, Boolean>,
            clickListener: LanguageListClickListener,
            position: Int
        ) {
            binding.language.text = languageCodeSelectionMap.keys.elementAt(0).split(":")[0]

            val isButtonSelected = languageCodeSelectionMap[languageCodeSelectionMap.keys.elementAt(0)]

            binding.languageSelector.isChecked = isButtonSelected == true

            binding.languageCodeContainer.setOnClickListener {
                binding.languageSelector.isChecked = true
                clickListener.onClick(languageCodeSelectionMap.keys.elementAt(0), position)
            }

            binding.languageSelector.setOnCheckedChangeListener { _, isSelected ->
                if (isSelected){
                    clickListener.onClick( languageCodeSelectionMap.keys.elementAt(0), position)
                }
            }
        }
    }
}


//OnClickListener for the RecyclerView
class LanguageListClickListener (val clickListener: (languageCode: String, position: Int) -> Unit){
    fun onClick(languageCode: String, position: Int) = clickListener(languageCode, position)
}