package app.mulipati.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentResetPasswordBinding


class ResetPasswordFragment : Fragment() {

    private lateinit var resetPasswordBinding: FragmentResetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resetPasswordBinding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        resetPasswordBinding.lifecycleOwner = this

        return resetPasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetPasswordBinding.passwordToSign.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }

        resetPasswordBinding.resetPasswordBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}