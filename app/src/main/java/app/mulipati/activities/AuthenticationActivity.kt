package app.mulipati.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import app.mulipati.MainActivity
import app.mulipati.R
import app.mulipati.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var authenticationBinding: ActivityAuthenticationBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authenticationBinding = DataBindingUtil.setContentView(this, R.layout.activity_authentication)

        sharedPreferences = getSharedPreferences(
            "remember_token", Context.MODE_PRIVATE
        )

        if (sharedPreferences.getString("token", "") != null){
            startActivity(
                Intent(
                    this, MainActivity::class.java
                )
            )
            overridePendingTransition(
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left
            )
        }

    }
}