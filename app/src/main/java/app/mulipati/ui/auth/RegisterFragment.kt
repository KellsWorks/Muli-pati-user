package app.mulipati.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.MainActivity
import app.mulipati.R
import app.mulipati.data.auth.Register
import app.mulipati.data.auth.RegisterResponse
import app.mulipati.databinding.FragmentRegisterBinding
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("DEPRECATION")
class RegisterFragment : Fragment() {

    private lateinit var registerBinding: FragmentRegisterBinding
    var v: Vibrator? = null

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
            v!!.vibrate(100)
            return false
        }
        if (registerBinding.registerPhone.text!!.length != 10){
            registerBinding.registerPhoneLayout.isErrorEnabled = true
            registerBinding.registerPhoneLayout.error = "Enter a 10 digit Malawian number"
            v!!.vibrate(100)
            return false
        }
        if (registerBinding.registerPassword.text.toString() != registerBinding.registerConfirmPassword.text.toString() ){
            registerBinding.registerPasswordLayout.isErrorEnabled = true
            registerBinding.registerPasswordLayout.error = "Passwords do not match"
            v!!.vibrate(100)
            return false
        }
        return true
    }

    private fun registerUser(
        user_name: String,
        user_mail: String,
        user_pass: String
    ) {

        val api = ApiClient.client!!.create(Routes::class.java)
        val register: Call<Register?>? = api.register(user_name, user_mail, user_pass)
        register!!.enqueue(object : Callback<Register?>{
            override fun onFailure(call: Call<Register?>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<Register?>, response: Response<RegisterResponse?>) {
                if (response.body()!!.token != null){

                }
            }

        })
    }
}

