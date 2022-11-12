package com.nativecitizens.globesurfer.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nativecitizens.globesurfer.R
import com.nativecitizens.globesurfer.databinding.FragmentDetailsBinding


class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding

    val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)


        binding.countryName.setOnClickListener {
            findNavController().popBackStack()
        }

        with(args.countryClicked){
            if (!this.flagUrl.isNullOrEmpty()){
                val uri = Uri.parse(this.flagUrl)
                Glide.with(requireContext())
                    .load(uri)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_image_loading_error))
                    .into(binding.countryFlag)
            }
            binding.countryName.text = this.name
            binding.population.text = this.population.toString()
            binding.continent.text = this.continent
            binding.capital.text = this.capital
            binding.language.text = this.officialLanguage
            binding.area.text = this.area.toString()
            binding.currency.text = this.currency
            binding.timeZone.text = this.timeZone
            binding.drivingSide.text = this.drivingSide
        }


        return binding.root
    }

}