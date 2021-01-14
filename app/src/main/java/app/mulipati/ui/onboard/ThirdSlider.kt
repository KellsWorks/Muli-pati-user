package app.mulipati.ui.onboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
            startActivity(Intent(requireActivity(), AuthenticationActivity::class.java))
            requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }
}