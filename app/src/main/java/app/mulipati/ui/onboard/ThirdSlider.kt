package app.mulipati.ui.onboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import app.mulipati.MainActivity
import app.mulipati.R
import app.mulipati.activities.AuthenticationActivity
import app.mulipati.databinding.FragmentThirdSliderBinding


class ThirdSlider : Fragment() {

    private lateinit var thirdSliderBinding: FragmentThirdSliderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        thirdSliderBinding = FragmentThirdSliderBinding.inflate(inflater, container, false)
        thirdSliderBinding.lifecycleOwner = this

        return thirdSliderBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        thirdSliderBinding.skip.setOnClickListener {

                val editor = requireActivity().getSharedPreferences("onBoard", Context.MODE_PRIVATE).edit()
                editor.putString("isFirstTime", "yes")
                editor.apply()

                startActivity(Intent(requireActivity(), AuthenticationActivity::class.java))
                requireActivity()
                    .overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        }

        val rotate: Animation = AnimationUtils.loadAnimation(requireContext().applicationContext,
            R.anim.rotate_clockwise
        )
        thirdSliderBinding.sliderImage.startAnimation(rotate)
    }
}