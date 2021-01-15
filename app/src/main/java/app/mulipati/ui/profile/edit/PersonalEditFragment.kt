package app.mulipati.ui.profile.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentPersonalEditBinding


class PersonalEditFragment : Fragment() {

    private lateinit var personalEditBinding: FragmentPersonalEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personalEditBinding = FragmentPersonalEditBinding.inflate(inflater, container, false)
        personalEditBinding.lifecycleOwner = this

        return personalEditBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personalEditBinding.personalEditBack.setOnClickListener {
            findNavController().navigateUp()
        }
        personalEditBinding.skipEditPersonal.setOnClickListener {
            findNavController().navigate(R.id.action_personalEditFragment_to_profileFragment)
        }
        personalEditBinding.editPersonal.setOnClickListener {
            findNavController().navigate(R.id.action_personalEditFragment_to_profileFragment)
        }
    }
}