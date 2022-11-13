package com.nativecitizens.globesurfer.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nativecitizens.globesurfer.R
import com.nativecitizens.globesurfer.adapters.ContinentListAdapter
import com.nativecitizens.globesurfer.adapters.ContinentListClickListener
import com.nativecitizens.globesurfer.adapters.TimeZoneListAdapter
import com.nativecitizens.globesurfer.adapters.TimeZoneListClickListener
import com.nativecitizens.globesurfer.databinding.FragmentDialogFilterBinding
import kotlinx.coroutines.launch


class DialogFilter : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogFilterBinding
    private var isDropDownContinent = false
    private var isDropDownTimeZone = false

    private val args: DialogFilterArgs by navArgs()

    private var selectedFilterList: MutableList<String> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_filter, container, false)


        val adapterContinent = ContinentListAdapter(ContinentListClickListener { continent ->
            if (!selectedFilterList.contains(continent))
                selectedFilterList.add(continent)
            else
                selectedFilterList.remove(continent)
        })

        val adapterTimeZone = TimeZoneListAdapter(TimeZoneListClickListener { timeZone ->
            if (!selectedFilterList.contains(timeZone))
                selectedFilterList.add(timeZone)
            else
                selectedFilterList.remove(timeZone)
        })

        val layoutManagerContinent = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        val layoutManagerTimeZone = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)


        binding.continentList.apply {
            adapter = adapterContinent
            layoutManager = layoutManagerContinent
        }
        binding.timeZoneList.apply {
            adapter = adapterTimeZone
            layoutManager = layoutManagerTimeZone
        }


        val continentList = resources.getStringArray(R.array.continent_list)
        adapterContinent.data = continentList.toList()
        args.timeZoneString.split(",").map { it.trim() }.toList().apply {
            adapterTimeZone.data = this.dropLast(1).sorted()
        }


        binding.continentDropDownBtn.setOnClickListener {
            lifecycleScope.launch {
                if (isDropDownContinent){
                    isDropDownContinent = false
                    binding.continentDropDownBtn.icon = ContextCompat
                        .getDrawable(requireContext(), R.drawable.ic_arrow_down)
                    binding.continentList.visibility = View.GONE
                } else {
                    isDropDownContinent = true
                    binding.continentDropDownBtn.icon = ContextCompat
                        .getDrawable(requireContext(), R.drawable.ic_arrow_up)
                    binding.continentList.visibility = View.VISIBLE
                    //Hide the TimeZone drop down
                    isDropDownTimeZone = false
                    binding.timeZoneList.visibility = View.GONE
                    binding.timeZoneDropDownBtn.icon = ContextCompat
                        .getDrawable(requireContext(), R.drawable.ic_arrow_down)
                }
            }
        }

        binding.timeZoneDropDownBtn.setOnClickListener {
            lifecycleScope.launch {
                if (isDropDownTimeZone){
                    isDropDownTimeZone = false
                    binding.timeZoneDropDownBtn.icon = ContextCompat
                        .getDrawable(requireContext(), R.drawable.ic_arrow_down)
                    binding.timeZoneList.visibility = View.GONE
                } else {
                    isDropDownTimeZone= true
                    binding.timeZoneDropDownBtn.icon = ContextCompat
                        .getDrawable(requireContext(), R.drawable.ic_arrow_up)
                    binding.timeZoneList.visibility = View.VISIBLE
                    //Hide the Continent drop down
                    isDropDownContinent = false
                    binding.continentList.visibility = View.GONE
                    binding.continentDropDownBtn.icon = ContextCompat
                        .getDrawable(requireContext(), R.drawable.ic_arrow_down)
                }
            }
        }


        val bundle = Bundle()
        binding.closeBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.resetBtn.setOnClickListener {
            bundle.putString("Reset", "")
            parentFragmentManager.setFragmentResult("ResetFeature", bundle)
            dialog?.dismiss()
        }

        binding.showResultBtn.setOnClickListener {
            bundle.putString("FilterString", selectedFilterList.joinToString(","))
            parentFragmentManager.setFragmentResult("Filter", bundle)
            dialog?.dismiss()
        }



        return binding.root
    }


    private fun onContinentListDropDown(){

    }


}