package com.nativecitizens.globesurfer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nativecitizens.globesurfer.R
import com.nativecitizens.globesurfer.adapters.CountryInitialListAdapter
import com.nativecitizens.globesurfer.adapters.CountryListAdapter
import com.nativecitizens.globesurfer.adapters.CountryListClickListener
import com.nativecitizens.globesurfer.databinding.FragmentSearchBinding
import com.nativecitizens.globesurfer.model.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchFragment @Inject constructor() : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search, container, false)

        val adapterCountryInitial = CountryInitialListAdapter()
        val layoutManagerCountryInitial = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)

        val adapterCountryViewList = CountryListAdapter(CountryListClickListener{

        })
        val layoutManagerCountryViewList = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)


        //Override OnBackPressed
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            requireActivity().finish()
        }


        return binding.root
    }
}