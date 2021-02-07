package app.mulipati.ui.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.databinding.FragmentTermsBinding


class TermsFragment : Fragment() {

    private lateinit var termsBinding: FragmentTermsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        termsBinding = FragmentTermsBinding.inflate(inflater, container, false)
        termsBinding.lifecycleOwner = this

        return termsBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        termsBinding.termsBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}