package app.mulipati.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentForgotPasswordResetBinding


class ForgotPasswordResetFragment : Fragment() {

    private lateinit var forgotPasswordBinding: FragmentForgotPasswordResetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        forgotPasswordBinding = FragmentForgotPasswordResetBinding.inflate(inflater, container, false)
        forgotPasswordBinding.lifecycleOwner = this

        return forgotPasswordBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        forgotPasswordBinding.confirmSMS.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPasswordResetFragment_to_resetPasswordFragment)
        }
    }
}