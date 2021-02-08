@file:Suppress("DEPRECATION")

package app.mulipati.ui.profile

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.activities.AuthenticationActivity
import app.mulipati.databinding.FragmentProfileBinding
import app.mulipati.util.Constants
import com.bumptech.glide.Glide


class ProfileFragment : Fragment() {

    private lateinit var profileBinding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        profileBinding = FragmentProfileBinding.inflate(inflater
            , container, false)
        profileBinding.lifecycleOwner = this

        return profileBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUser()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun bindUser(){

        val userPreferences = context?.getSharedPreferences("user", Context.MODE_PRIVATE)

        profileBinding.username.text = userPreferences?.getString("name", "")

        val phoneUtil = PhoneNumberUtils.formatNumber(userPreferences?.getString("phone", ""), "MW")
        val phone =removeFirstChar(phoneUtil)

        profileBinding.userNumber.text = "+265 $phone"

        Glide
            .with(requireContext())
            .load(Constants.PROFILE_URL+userPreferences?.getString("photo", ""))
            .centerCrop()
            .into(profileBinding.userAvatar)
    }

    private fun removeFirstChar(s: String): String? {
        return s.substring(1)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        profileBinding.notifictions.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_notificationsFragment)
        }

        profileBinding.toSupport.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_supportFragment)
        }

        profileBinding.toTrips.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_tripsFragment)
        }

        profileBinding.toSettings.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        profileBinding.toPersonal.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_personalFragment)
        }

        profileBinding.toTerms.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_termsFragment)
        }

        profileBinding.editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editImageFragment2)
        }

        profileBinding.logOut.setOnClickListener {

            val rotate: Animation = AnimationUtils.loadAnimation(requireContext().applicationContext,
                    R.anim.rotate_clockwise
            )

            profileBinding.logoutIcon.setImageResource(R.drawable.ic_restore)
            profileBinding.logoutIcon.startAnimation(rotate)

            Handler().postDelayed({
                profileBinding.logoutIcon.setImageResource(R.drawable.ic_logout)
            }, 5000)

            logOut()
        }
    }

    private fun logOut(){

        val userPreferences = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.edit()
        val dialog = ProgressDialog(requireContext())
        dialog.setMessage("Logging out...")
        dialog.setCancelable(false)

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {

                    dialog.show()

                    userPreferences?.clear()
                    userPreferences?.apply()

                    startActivity(
                            Intent(
                                    requireActivity(), AuthenticationActivity::class.java
                            )
                    )

                    dialog.dismiss()
                }
                DialogInterface.BUTTON_NEGATIVE -> {}
                DialogInterface.BUTTON_NEUTRAL -> {}
            }
        }

        val mBuilder = AlertDialog.Builder(requireContext())
                .setTitle("Log out")
                .setMessage("By logging out, all your account preferences will be lost. Do you wish to continue?")
                .setIcon(R.drawable.ic_icon)
                .setNegativeButton("No", dialogClickListener)
                .setPositiveButton("Yes", dialogClickListener)

        mBuilder.show()
    }
}