package app.mulipati.ui.onboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import app.mulipati.R
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val rotate: Animation = AnimationUtils.loadAnimation(requireContext().applicationContext,
            R.anim.rotate_clockwise
        )
        secondSliderBinding.secondImage.startAnimation(rotate)
    }

}