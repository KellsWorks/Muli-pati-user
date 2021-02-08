@file:Suppress("DEPRECATION")

package app.mulipati.ui.personal

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.activities.OnBoarding
import app.mulipati.databinding.FragmentPersonalBinding
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.network.responses.account.Delete
import app.mulipati.util.Constants
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalFragment : Fragment() {

    private lateinit var personalBinding: FragmentPersonalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        personalBinding = FragmentPersonalBinding.inflate(inflater, container, false)
        personalBinding.lifecycleOwner = this

         return personalBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bindUser()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        personalBinding.personalBack.setOnClickListener {
            findNavController().navigateUp()
        }

        bindCounts()

        personalBinding.deleteAccount.setOnClickListener {
            deleteAccount()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun bindUser(){

        val userPreferences = context?.getSharedPreferences("user", Context.MODE_PRIVATE)


        if(userPreferences?.getString("name", "")!!.isEmpty()){
            personalBinding.personalName.text = "Not set"
        }else{
            personalBinding.personalName.text = userPreferences.getString("name", "")
        }

        if(userPreferences.getString("email", "")!!.isEmpty()){
            personalBinding.personalName.text = "Not set"
        }else{
            personalBinding.personalEmail.text = userPreferences.getString("email", "")
        }

        val phoneUtil = PhoneNumberUtils.formatNumber(userPreferences.getString("phone", ""), "MW")
        val phone =removeFirstChar(phoneUtil)

        personalBinding.personalPhone.text = "+265 $phone"

        personalBinding.membership.text = "Member since: " + userPreferences.getString("membership", "")
        personalBinding.personalLocation.text = userPreferences.getString("location", "") + ", Malawi"

        Glide
                .with(requireContext())
                .load(Constants.PROFILE_URL+ userPreferences.getString("photo", ""))
                .centerCrop()
                .into(personalBinding.personalIcon)
    }

    private fun removeFirstChar(s: String): String? {
        return s.substring(1)
    }

    private fun bindCounts(){

        val tripsPreferences = context?.getSharedPreferences("trips_count", Context.MODE_PRIVATE)
        val upcomingCount = tripsPreferences!!.getString("upcomingCount", "?")
        val completedCount = tripsPreferences.getString("completedCount", "?")
        val cancelledCount = tripsPreferences.getString("cancelledCount", "?")


        personalBinding.upcomingCount.text = upcomingCount
        personalBinding.completedCount.text = completedCount
        personalBinding.cancelledCount.text = cancelledCount

    }

    private fun deleteAccount(){

        val dialogClickListener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {

                    onDelete()
                }
                DialogInterface.BUTTON_NEGATIVE -> {}
                DialogInterface.BUTTON_NEUTRAL -> {}
            }
        }

        val mBuilder = AlertDialog.Builder(requireContext())
                .setTitle("Delete account")
                .setMessage("By deleting account, all your account preferences will be lost. Do you wish to continue?")
                .setIcon(R.drawable.ic_icon)
                .setNegativeButton("No", dialogClickListener)
                .setPositiveButton("Yes", dialogClickListener)

        mBuilder.show()
    }

    private fun onDelete(){

        val dialog = ProgressDialog(requireContext())
        dialog.setMessage("Processing...")
        dialog.setCancelable(false)

        dialog.show()

        val user = context?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val id = user?.getInt("id", 0)


        val api = ApiClient.client!!.create(Routes::class.java)

        val delete: Call<Delete?>? = api.accountDelete(id)
        delete?.enqueue(object : Callback<Delete?> {
            override fun onFailure(call: Call<Delete?>, t: Throwable) {
                Toast.makeText(
                        requireContext(),
                        "A network error occurred",
                        Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }

            override fun onResponse(call: Call<Delete?>, response: Response<Delete?>) {

                when(response.code())
                {
                    200->{
                        clearPrefs()

                        dialog.dismiss()

                        requireActivity()
                                .startActivity(
                                        Intent(
                                                requireActivity(), OnBoarding::class.java
                                        )
                                )
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

    private fun clearPrefs(){

        val userPref = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.edit()
        val rememberToken = context?.getSharedPreferences("remember_token", Context.MODE_PRIVATE)?.edit()
        val appTheme = context?.getSharedPreferences("appTheme", Context.MODE_PRIVATE)?.edit()

        val trips = context?.getSharedPreferences("trips", Context.MODE_PRIVATE)?.edit()

        userPref?.clear()
        userPref?.apply()
        rememberToken?.clear()

        rememberToken?.apply()

        appTheme?.clear()
        appTheme?.apply()

        trips?.clear()
        trips?.apply()

    }

}