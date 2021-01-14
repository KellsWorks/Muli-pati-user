package app.mulipati.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import app.mulipati.R
import app.mulipati.adapters.PagerAdapter
import app.mulipati.databinding.ActivityOnBoardingBinding
import app.mulipati.ui.onboard.FirstSlider
import app.mulipati.ui.onboard.SecondSlider

class OnBoarding : AppCompatActivity() {

    private lateinit var onBoardingBinding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBoardingBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)

        setupViewPager()
    }

    private fun setupViewPager() {

        val adapter = PagerAdapter(supportFragmentManager)

        adapter.addFragment(FirstSlider() , " One ")
        adapter.addFragment(SecondSlider() , " two ")

        onBoardingBinding.onBoardPager.adapter = adapter



    }
}