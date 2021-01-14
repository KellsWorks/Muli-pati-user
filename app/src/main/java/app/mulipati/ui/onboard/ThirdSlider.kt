package app.mulipati.ui.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
}