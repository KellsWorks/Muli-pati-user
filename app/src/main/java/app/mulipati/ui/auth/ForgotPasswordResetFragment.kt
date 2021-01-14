package app.mulipati.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentForgotPasswordBinding


class ForgotPasswordResetFragment : Fragment() {

    private lateinit var forgotPasswordBinding: FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        forgotPasswordBinding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        forgotPasswordBinding.lifecycleOwner = this

        return forgotPasswordBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        forgotPasswordBinding.sendSMS.setOnClickListener {
            findNavController().navigate(R.id.action_forgotPasswordResetFragment_to_resetPasswordFragment)
        }
    }
}