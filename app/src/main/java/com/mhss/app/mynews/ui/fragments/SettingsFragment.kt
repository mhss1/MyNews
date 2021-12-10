package com.mhss.app.mynews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.*
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.mhss.app.mynews.R
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.map
import com.mhss.app.mynews.databinding.FragmentSettingsBinding
import com.mhss.app.mynews.util.Constants
import com.mhss.app.mynews.util.countryToCode

class SettingsFragment : Fragment(), OnItemSelectedListener {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.countries_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.countrySpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.languages_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.languagesSpinner.adapter = adapter
        }

        readSettings(Constants.COUNTRY_SETTINGS).observe(viewLifecycleOwner){
                binding.countrySpinner.setSelection(
                    resources.getStringArray(R.array.countries_list).indexOf(it)
                )
        }

        readSettings(Constants.LANGUAGE_SETTINGS).observe(viewLifecycleOwner){
            binding.languagesSpinner.setSelection(
                resources.getStringArray(R.array.languages_list).indexOf(it)
            )
        }

        binding.countrySpinner.onItemSelectedListener = this
        binding.languagesSpinner.onItemSelectedListener = this

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.country_spinner -> editSettings(
                    Constants.COUNTRY_SETTINGS,
                    parent.getItemAtPosition(position).toString())
            R.id.languages_spinner ->{
                editSettings(
                    Constants.LANGUAGE_SETTINGS,
                    parent.getItemAtPosition(position).toString())
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun editSettings(key: String, value: String) {
        lifecycleScope.launch {
            requireContext().dataStore.edit { settings ->
                settings[stringPreferencesKey(key)] = value
            }
        }
    }

    private fun readSettings(key: String) =
        requireContext().dataStore.data
            .map { preferences ->
                preferences[stringPreferencesKey(key)] ?: ""
            }.asLiveData()
}