package com.nativecitizens.globesurfer.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nativecitizens.globesurfer.R
import com.nativecitizens.globesurfer.adapters.LanguageListAdapter
import com.nativecitizens.globesurfer.adapters.LanguageListClickListener
import com.nativecitizens.globesurfer.databinding.FragmentDialogLanguageBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



class DialogLanguage : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogLanguageBinding
    private val args: DialogLanguageArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_language,
            container, false)

        binding.closeBtn.setOnClickListener {
            dialog?.dismiss()
        }

        val languagesList = resources.getStringArray(R.array.language_list)
        val map: MutableList<Map<String, Boolean>> = mutableListOf()
        languagesList.toList().forEach { code ->
            if (code == args.currentLanguageCode){
                map.add(mapOf(code to true))
            } else {
                map.add(mapOf(code to false))
            }
        }

        val adapterLanguageList = LanguageListAdapter(LanguageListClickListener{ languageCode, position ->
            lifecycleScope.launch {
                delay(300)
                val bundle = Bundle()
                bundle.putString("SelectedLanguage", languageCode)
                bundle.putInt("Position", position)
                parentFragmentManager.setFragmentResult("Language", bundle)
                dialog?.dismiss()
            }
        })
        val layoutManagerLanguageList = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,false)

        binding.languageList.apply {
            adapter = adapterLanguageList
            layoutManager = layoutManagerLanguageList
        }
        adapterLanguageList.data = map


        return binding.root
    }

}