package app.mulipati.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var registerBinding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        registerBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        registerBinding.lifecycleOwner = this

        return registerBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        registerBinding.RegisterButton.setOnClickListener {
            if (validate()){
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }

        registerBinding.toSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun validate(): Boolean{

        if (registerBinding.registerUsername.text.toString().isEmpty()){
            registerBinding.registerUsernameLayout.isErrorEnabled = true
            registerBinding.registerUsernameLayout.error = "This field is required"
            return false
        }
        if (registerBinding.registerPhone.text!!.length != 10){
            registerBinding.registerPhoneLayout.isErrorEnabled = true
            registerBinding.registerPhoneLayout.error = "Enter a 10 digit Malawian number"
            return false
        }
        if (registerBinding.registerPassword.text.toString() != registerBinding.registerConfirmPassword.text.toString() ){
            registerBinding.registerPasswordLayout.isErrorEnabled = true
            registerBinding.registerPasswordLayout.error = "Passwords do not match"
            return false
        }
        return true
    }
}