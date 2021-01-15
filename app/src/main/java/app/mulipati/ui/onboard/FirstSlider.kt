package app.mulipati.ui.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import app.mulipati.R
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val rotate: Animation = AnimationUtils.loadAnimation(requireContext().applicationContext,
            R.anim.rotate_clockwise
        )
        firstSliderBinding.firstImage.startAnimation(rotate)
    }

}