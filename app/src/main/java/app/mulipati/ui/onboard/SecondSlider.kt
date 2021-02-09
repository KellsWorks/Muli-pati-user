package app.mulipati.ui.onboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.mulipati.databinding.FragmentSecondSliderBinding


class SecondSlider : Fragment() {

    private lateinit var secondSliderBinding: FragmentSecondSliderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        secondSliderBinding =  FragmentSecondSliderBinding.inflate(inflater, container, false)
        secondSliderBinding.lifecycleOwner = this

        return secondSliderBinding.root
    }


}