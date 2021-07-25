@file:Suppress("DEPRECATION")

package app.mulipati

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import app.mulipati.databinding.ActivityMainBinding
import app.mulipati.network.BackgroundServices
import com.google.firebase.iid.FirebaseInstanceId
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpNavigation()

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            BackgroundServices().sendTokenToServer(instanceIdResult.token, 1)
        }

        Toast.makeText(this, "This is a beta build, bugs may occur", Toast.LENGTH_SHORT).show()

    }

    private fun setUpNavigation(){

        val navController = findNavController(R.id.nav_host_fragment)

        binding.bottomMenu.setupWithNavController(navController)
    }
}