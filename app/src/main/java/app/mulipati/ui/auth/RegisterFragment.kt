@file:Suppress("DEPRECATION")

package app.mulipati.ui.auth

import android.app.ProgressDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.R
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
                registerUser(
                    registerBinding.registerUsername.text.toString(),
                    registerBinding.registerPhone.text.toString(),
                    registerBinding.registerConfirmPassword.text.toString()
                )
            }else{
                Toast.makeText(
                    requireContext(),
                    "Error, check your input",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        registerBinding.toSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun validate(): Boolean{

        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        if (registerBinding.registerUsername.text.toString().isEmpty()){
            registerBinding.registerUsernameLayout.isErrorEnabled = true
            registerBinding.registerUsernameLayout.error = "This field is required"

            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }

            return false
        }
        if (registerBinding.registerPhone.text!!.length != 10){
            registerBinding.registerPhoneLayout.isErrorEnabled = true
            registerBinding.registerPhoneLayout.error = "Enter a 10 digit Malawian number"

            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }

            return false
        }
        if (registerBinding.registerPassword.text.toString() != registerBinding.registerConfirmPassword.text.toString() ){
            registerBinding.registerPasswordLayout.isErrorEnabled = true
            registerBinding.registerPasswordLayout.error = "Passwords do not match"

            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }

            return false
        }
        return true
    }

    private fun registerUser(
        user_name: String,
        user_phone: String,
        user_pass: String
    ) {

        val dialog = ProgressDialog(requireContext())
        dialog.setMessage("Registering...")
        dialog.setCancelable(false)

        dialog.show()


        val api = ApiClient.client!!.create(Routes::class.java)

        val register: Call<RegisterResponse?>? = api.register(user_name, user_phone, user_pass)
        register?.enqueue(object : Callback<RegisterResponse?>{
            override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "A network error occurred...",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }

            override fun onResponse(call: Call<RegisterResponse?>, response: Response<RegisterResponse?>) {

                val tokenEdit = requireActivity()
                    .getSharedPreferences("remember_token", Context.MODE_PRIVATE)
                    .edit()
                tokenEdit.putString("token", response.body()?.token)
                tokenEdit.apply()

                Toast.makeText(
                    requireContext(),
                    "Registration successful!",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()

                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

                dialog.dismiss()
            }

        })
    }
}

