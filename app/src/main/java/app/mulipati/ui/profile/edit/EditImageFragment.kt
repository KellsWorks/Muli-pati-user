package app.mulipati.ui.profile.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentEditImageBinding


class EditImageFragment : Fragment() {

    private lateinit var editImageBinding: FragmentEditImageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        editImageBinding = FragmentEditImageBinding.inflate(inflater, container, false)
        editImageBinding.lifecycleOwner = this

        return editImageBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        editImageBinding.toEditPersonal.setOnClickListener {
            findNavController().navigate(R.id.action_editImageFragment2_to_personalEditFragment)
        }

        editImageBinding.iconEditBack.setOnClickListener {
            findNavController().navigateUp()
        }

        editImageBinding.skipEditIcon.setOnClickListener {
            findNavController().navigate(R.id.action_editImageFragment2_to_personalEditFragment)
        }
    }
}