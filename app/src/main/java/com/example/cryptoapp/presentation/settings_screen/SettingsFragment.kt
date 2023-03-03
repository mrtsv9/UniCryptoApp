package com.example.cryptoapp.presentation.settings_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.FragmentSettingsBinding
import com.example.cryptoapp.presentation.base.BaseFragment

class SettingsFragment: BaseFragment<FragmentSettingsBinding>() {

    private lateinit var toolbar: Toolbar


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSettingsBinding
        get() = FragmentSettingsBinding::inflate

    override fun setup() {

        toolbar = binding.settingToolbar
        toolbar.title = "Settings"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))

    }
}