package com.nativecitizens.globesurfer.ui

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nativecitizens.globesurfer.R
import com.nativecitizens.globesurfer.adapters.CountryInitialListAdapter
import com.nativecitizens.globesurfer.adapters.CountryListClickListener
import com.nativecitizens.globesurfer.databinding.FragmentSearchBinding
import com.nativecitizens.globesurfer.model.Country
import com.nativecitizens.globesurfer.model.SearchViewModel
import com.nativecitizens.globesurfer.util.ResponseState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject



@AndroidEntryPoint
class SearchFragment @Inject constructor() : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val searchViewModel: SearchViewModel by activityViewModels()

    private var selectedLanguageCode = "English:en"

    private var isDarkModeActive: Boolean = false


    override fun onStart() {
        super.onStart()
        when(requireContext().resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)){
            Configuration.UI_MODE_NIGHT_YES -> {
                //Night mode is active
                isDarkModeActive = true
                binding.darkLightModeSelector.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_light_mode)
                //change to dark mode to follow system setting, if user previously changed theme in-app
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                //Light mode is active
                isDarkModeActive = false
                binding.darkLightModeSelector.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_dark_mode)
                //change to light mode to follow system setting, if user previously changed theme in-app
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search, container, false)


        //Override OnBackPressed
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            requireActivity().finish()
        }


        val adapterCountryInitial = CountryInitialListAdapter(CountryListClickListener {
            findNavController().navigate(SearchFragmentDirections
                .actionSearchFragmentToDetailsFragment(it))
        })
        val layoutManagerCountryInitial = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)


        binding.countryListParent.apply {
            adapter = adapterCountryInitial
            layoutManager = layoutManagerCountryInitial
        }

        binding.searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(search: Editable?) {
                searchViewModel.searchCountry(search.toString())
            }
        })

        binding.languageBtn.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDialogLanguage(selectedLanguageCode))
        }
        binding.filterBtn.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections
                .actionSearchFragmentToDialogFilter(searchViewModel.getTimeZones()))
        }
        binding.darkLightModeSelector.setOnClickListener {
            if (isDarkModeActive){
                //change to light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                //change to dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }


        parentFragmentManager.setFragmentResultListener(
            "Language",
            viewLifecycleOwner
        ) { req, res ->
            if (req == "Language") {
                selectedLanguageCode = res.getString("SelectedLanguage") ?: "English:en"
                binding.languageBtn.text = res.getString("SelectedLanguage")?.split(":")?.get(1) ?: "en"
                searchViewModel.setSelectedTranslation(res.getString("SelectedLanguage")?.split(":")?.get(1) ?: "en")
            }
        }

        parentFragmentManager.setFragmentResultListener(
            "Filter",
            viewLifecycleOwner
        ) { req, res ->
            if (req == "Filter") {
                searchViewModel.setFilter(res.getString("FilterString") ?: "")
            }
        }

        parentFragmentManager.setFragmentResultListener(
            "ResetFeature",
            viewLifecycleOwner
        ) { req, _ ->
            if (req == "ResetFeature") {
                searchViewModel.resetFilter()
            }
        }


        searchViewModel.countryListResponse.observe(viewLifecycleOwner){
            when(it){
                is ResponseState.Loading -> {
                    binding.loadingProgress.visibility = View.VISIBLE
                    binding.connectionError.visibility = View.GONE
                    binding.searchBox.isEnabled = false
                }
                is ResponseState.Error -> {
                    binding.searchBox.isEnabled = false
                    binding.loadingProgress.visibility = View.GONE
                    binding.connectionError.visibility = View.VISIBLE

                    Snackbar.make(binding.root, "Network error! Check your internet connection.", 4_000)
                        .show()

                    searchViewModel.prepareForReconnection()
                }
                is ResponseState.Success -> {
                    binding.searchBox.isEnabled = true
                    binding.filterBtn.isEnabled = true
                    binding.languageBtn.isEnabled = true

                    if (it.response?.isNotEmpty() == true) {
                        val countryInitialsList: MutableList<String> = mutableListOf()
                        val listOfMapOfCountries: MutableList<Map<String, List<Country>>> = mutableListOf()

                        it.response.forEach {country ->
                            countryInitialsList.add(country.name?.get(0).toString())
                        }

                        countryInitialsList.distinct().sorted().forEach { initial ->
                            val countryList: MutableList<Country> = mutableListOf()

                            it.response.forEach { country ->
                                if (country.name?.startsWith(initial) == true){
                                    countryList.add(country)
                                }
                            }
                            listOfMapOfCountries.add(mapOf(initial to countryList.toList()))
                        }

                        adapterCountryInitial.data = listOfMapOfCountries

                    }
                    binding.loadingProgress.visibility = View.GONE
                    binding.connectionError.visibility = View.GONE
                }
                else -> {}
            }
        }


        return binding.root
    }
}