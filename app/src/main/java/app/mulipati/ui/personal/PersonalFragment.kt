package app.mulipati.ui.personal

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.databinding.FragmentPersonalBinding
import app.mulipati.util.Constants
import com.bumptech.glide.Glide
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

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

//        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//        val date = LocalDate.parse("31-12-2018", formatter)
//

        personalBinding.personalPhone.text = userPreferences.getString("phone", "")
        personalBinding.membership.text = "Member since: " + userPreferences.getString("membership", "")
        personalBinding.personalLocation.text = userPreferences.getString("location", "") + ", Malawi"

        Glide
                .with(requireContext())
                .load(Constants.PROFILE_URL+ userPreferences.getString("photo", ""))
                .centerCrop()
                .into(personalBinding.personalIcon)
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

}