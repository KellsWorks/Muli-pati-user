package app.mulipati.ui.auth

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
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
import app.mulipati.MainActivity
import app.mulipati.R
import app.mulipati.data.User
import app.mulipati.databinding.FragmentLoginBinding
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        loginBinding.lifecycleOwner = this

        return loginBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loginBinding.createAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        loginBinding.SignInButton.setOnClickListener {
            if (validate()){
                loginUser(
                    loginBinding.signInName.text.toString(),
                    loginBinding.signInNamePassword.text.toString()
                )
            }
        }

        loginBinding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    private fun loginUser(
        user_phone: String,
        user_pass: String
    ){
        val dialog = ProgressDialog(requireContext())
        dialog.setMessage("Signing in...")
        dialog.setCancelable(false)

        dialog.show()

        val api = ApiClient.client!!.create(Routes::class.java)

        val register: Call<User?>? = api.login(user_phone, user_pass)
        register?.enqueue(object : Callback<User?> {
            override fun onFailure(call: Call<User?>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }

            override fun onResponse(call: Call<User?>, response: Response<User?>) {

                Toast.makeText(
                    requireContext(),
                    "Success",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
                
                val editor = requireActivity().getSharedPreferences("onBoard", Context.MODE_PRIVATE).edit()
                editor.putString("isFirstTime", "yes")
                editor.apply()

                val userPreferences = context?.getSharedPreferences(
                    "user", Context.MODE_PRIVATE
                )?.edit()

                response.body()?.id?.let { userPreferences?.putInt("id", it) }
                userPreferences?.putString("name", response.body()?.name)
                userPreferences?.putString("phone", response.body()?.phone)
                userPreferences?.putString("photo", response.body()?.profile?.get(0)?.photo)
                userPreferences?.putString("email", response.body()?.profile?.get(0)?.email)
                userPreferences?.putString("role", response.body()?.profile?.get(0)?.role)
                userPreferences?.putString("location", response.body()?.profile?.get(0)?.location)
                userPreferences?.putString("membership", response.body()?.membership)

                userPreferences?.apply()

                requireActivity().startActivity(Intent(
                    requireActivity(), MainActivity::class.java
                ))
                requireActivity()
                    .overridePendingTransition(
                        android.R.anim.slide_out_right, android.R.anim.slide_in_left
                    )

                dialog.dismiss()
            }

        })
    }

    private fun validate(): Boolean{

        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        if (loginBinding.signInName.text.toString().isEmpty()){
            loginBinding.signInNameLayout.isErrorEnabled = true
            loginBinding.signInNameLayout.error = "This field is required"

            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }

            return false
        }

        if (loginBinding.signInNamePassword.text.toString().isEmpty()){
            loginBinding.signInNamePasswordLayout.isErrorEnabled = true
            loginBinding.signInNamePasswordLayout.error = "Enter your password"

            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }

            return false
        }

        return true
    }
}