package app.mulipati.ui.personal

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.databinding.FragmentPersonalBinding
import app.mulipati.util.Constants
import com.bumptech.glide.Glide

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

        bindUser()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        personalBinding.personalBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

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

        personalBinding.personalPhone.text = userPreferences.getString("phone", "")
        personalBinding.personalLocation.text = userPreferences.getString("location", "") + ", Malawi"

        Glide
                .with(requireContext())
                .load(Constants.PROFILE_URL+ userPreferences.getString("photo", ""))
                .centerCrop()
                .into(personalBinding.personalIcon)
    }

}