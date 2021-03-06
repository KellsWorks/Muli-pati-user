package app.mulipati.ui.settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var settingsBinding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        settingsBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        settingsBinding.lifecycleOwner = this

        return settingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsBinding.settingsBack.setOnClickListener {
            findNavController().navigateUp()
        }

        settingsBinding.toggleLocation.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_settingNotificationFragment)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val permission = ContextCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.ACCESS_NOTIFICATION_POLICY)

        val location = ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission == PackageManager.PERMISSION_GRANTED) {
            settingsBinding.toggleNotifications.isActivated = true
            settingsBinding.toggleNotifications.isChecked = true
        }
        else{
            if (permission == PackageManager.PERMISSION_GRANTED) {
                settingsBinding.toggleNotifications.isActivated = true
                settingsBinding.toggleNotifications.isChecked = true
            }
        }

        if(location == PackageManager.PERMISSION_GRANTED){
            settingsBinding.toggleLocation.isActivated = true
            settingsBinding.toggleLocation.isChecked = true
        }

        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                settingsBinding.toggleTheme.isChecked = true
                settingsBinding.toggleTheme.isActivated = true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                settingsBinding.toggleTheme.isChecked = false
                settingsBinding.toggleTheme.isActivated = false
            }

        }

        if (settingsBinding.toggleTheme.isChecked && settingsBinding.toggleTheme.isActivated){
            settingsBinding.toggleTheme.setOnClickListener {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                val preferences = requireActivity().getSharedPreferences("appTheme", Context.MODE_PRIVATE).edit()
                preferences.putBoolean("isDarkTheme", false)
                preferences.apply()
            }
        }else{
            settingsBinding.toggleTheme.setOnClickListener {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    Configuration.UI_MODE_NIGHT_NO ->
                    {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        val preferences = requireActivity().getSharedPreferences("appTheme", Context.MODE_PRIVATE).edit()
                        preferences.putBoolean("isDarkTheme", true)
                        preferences.apply()
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                        Toast.makeText(requireContext(), "Your device does not support this.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }


    }
}