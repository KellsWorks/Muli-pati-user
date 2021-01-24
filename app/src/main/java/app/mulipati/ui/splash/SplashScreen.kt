package app.mulipati.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import app.mulipati.MainActivity
import app.mulipati.activities.OnBoarding

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = getSharedPreferences("appTheme", Context.MODE_PRIVATE)
        val theme = preferences.getBoolean("isDarkTheme", true)

        if (theme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val token = getSharedPreferences("user", Context.MODE_PRIVATE)?.getString("token", "")

        if (token != ""){
            startActivity(Intent(
                    this, MainActivity::class.java
            ))
            overridePendingTransition(
                    android.R.anim.slide_out_right, android.R.anim.slide_in_left
            )
        }else{
            startActivity(Intent(this, OnBoarding::class.java))

        }
    }
}