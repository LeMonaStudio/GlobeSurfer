package com.nativecitizens.globesurfer.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nativecitizens.globesurfer.R
import com.nativecitizens.globesurfer.databinding.FragmentDialogLanguageBinding


class DialogLanguage : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDialogLanguageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_language,
            container, false)


        return binding.root
    }

}