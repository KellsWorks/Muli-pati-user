package app.mulipati.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import app.mulipati.databinding.FragmentSettingNotificationBinding


class SettingNotificationFragment : Fragment() {

    private lateinit var settingNotificationBinding: FragmentSettingNotificationBinding
    private val requestCode = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingNotificationBinding = FragmentSettingNotificationBinding.inflate(inflater, container, false)
        settingNotificationBinding.lifecycleOwner = this

        return settingNotificationBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingNotificationBinding.denyNotificationRequest.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        } else {
            TODO("VERSION.SDK_INT < M")
        }


        settingNotificationBinding.requestNotification.setOnClickListener {
            if (permission != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestNotificationsPermissions()
                }
            }
            else{
                Toast.makeText(requireContext(), "Permission is already granted!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun requestNotificationsPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
                requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            this.requestCode -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i("TAG", "Permission has been denied by user")
                } else {
                    Log.i("TAG", "Permission has been granted by user")
                }
            }
        }
    }
}