package app.mulipati.ui.support

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentSupportBinding


class SupportFragment : Fragment() {

    private lateinit var supportBinding: FragmentSupportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        supportBinding = FragmentSupportBinding.inflate(inflater, container, false)
        supportBinding.lifecycleOwner = this

        return supportBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        supportBinding.supportBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}