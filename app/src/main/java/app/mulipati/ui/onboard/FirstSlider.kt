package app.mulipati.ui.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.mulipati.databinding.FragmentFirstSliderBinding

class FirstSlider : Fragment() {

    private lateinit var firstSliderBinding: FragmentFirstSliderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firstSliderBinding =  FragmentFirstSliderBinding.inflate(inflater, container, false)
        firstSliderBinding.lifecycleOwner = this

        return firstSliderBinding.root
    }


}