package app.mulipati.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentProfileBinding


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
    }
}