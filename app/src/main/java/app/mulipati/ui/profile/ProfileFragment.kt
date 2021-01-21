package app.mulipati.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentProfileBinding
import app.mulipati.util.Constants
import com.bumptech.glide.Glide


class ProfileFragment : Fragment() {

    private lateinit var profileBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        profileBinding = FragmentProfileBinding.inflate(inflater
            , container, false)
        profileBinding.lifecycleOwner = this

        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUser()
    }

    private fun bindUser(){

        val userPreferences = context?.getSharedPreferences("user", Context.MODE_PRIVATE)

        profileBinding.username.text = userPreferences?.getString("name", "")
        profileBinding.userNumber.text = userPreferences?.getString("phone", "")

        Glide
            .with(requireContext())
            .load(Constants.PROFILE_URL+userPreferences?.getString("photo", ""))
            .centerCrop()
            .into(profileBinding.userAvatar)
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
    }
}