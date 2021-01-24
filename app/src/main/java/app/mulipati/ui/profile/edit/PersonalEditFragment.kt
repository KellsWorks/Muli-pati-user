@file:Suppress("DEPRECATION")

package app.mulipati.ui.profile.edit

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
import app.mulipati.databinding.FragmentPersonalEditBinding
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.network.responses.account.AccountUpdateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PersonalEditFragment : Fragment() {

    private lateinit var personalEditBinding: FragmentPersonalEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        personalEditBinding = FragmentPersonalEditBinding.inflate(inflater, container, false)
        personalEditBinding.lifecycleOwner = this

        return personalEditBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        personalEditBinding.personalEditBack.setOnClickListener {
            findNavController().navigateUp()
        }
        personalEditBinding.skipEditPersonal.setOnClickListener {
            findNavController().navigate(R.id.action_personalEditFragment_to_profileFragment)
        }
        personalEditBinding.editPersonal.setOnClickListener {
            if (validate()){
                updateAccount(
                        personalEditBinding.editName.text.toString(),
                        personalEditBinding.editEmail.text.toString(),
                        personalEditBinding.editPhoneNumber.text.toString()
                )
            }

        }
    }

    private fun validate(): Boolean{

        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator


        if (personalEditBinding.editName.text.toString().isEmpty()){
            Toast.makeText(
                    requireContext(), "This field is required", Toast.LENGTH_SHORT
            ).show()
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }

            return false
        }
        if (personalEditBinding.editPhoneNumber.text!!.length != 10){
            Toast.makeText(
                    requireContext(), "Enter a 10 digit Malawian number", Toast.LENGTH_SHORT
            ).show()
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }

            return false
        }
        if (personalEditBinding.editEmail.text.isNullOrEmpty() ){
            Toast.makeText(
                    requireContext(), "This field is required", Toast.LENGTH_SHORT
            ).show()

            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }

            return false
        }
        return true
    }

    private fun updateAccount(
            name: String,
            email: String,
            phone: String
    ){
        val dialog = ProgressDialog(requireContext())
        dialog.setMessage("Updating...")
        dialog.setCancelable(false)

        dialog.show()

        val user = context?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val id = user?.getInt("profile_id", 0)


        val api = ApiClient.client!!.create(Routes::class.java)

        val account: Call<AccountUpdateResponse?>? = api.accountUpdate(id, name, email, phone)
        account?.enqueue(object : Callback<AccountUpdateResponse?> {
            override fun onFailure(call: Call<AccountUpdateResponse?>, t: Throwable) {
                Toast.makeText(
                        requireContext(),
                        "A network error occurred",
                        Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }

            override fun onResponse(call: Call<AccountUpdateResponse?>, response: Response<AccountUpdateResponse?>) {

                when(response.code())
                {
                    200->{

                        val userPref = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.edit()
                        userPref?.putString("name", response.body()?.user?.name)
                        userPref?.putString("phone", response.body()?.user?.phone)
                        userPref?.putString("email", response.body()?.profile?.email)
                        userPref?.apply()

                        Toast.makeText(
                                requireContext(),
                                "Update success!",
                                Toast.LENGTH_SHORT
                        ).show()

                        dialog.dismiss()

                        findNavController().navigate(R.id.action_personalEditFragment_to_profileFragment)
                    }
                    500 ->{

                        Toast.makeText(
                                requireContext(),
                                "A server error occurred",
                                Toast.LENGTH_SHORT
                        ).show()

                        dialog.dismiss()
                    }
                }

            }

        })
    }
}