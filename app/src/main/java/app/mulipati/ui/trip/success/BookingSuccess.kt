package app.mulipati.ui.trip.success

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.databinding.FragmentBookingSuccessBinding

@Suppress("DEPRECATION")
class BookingSuccess : Fragment() {

    private lateinit var bookingSuccessBinding: FragmentBookingSuccessBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        bookingSuccessBinding = FragmentBookingSuccessBinding.inflate(inflater, container, false)
        bookingSuccessBinding.lifecycleOwner = this

        return bookingSuccessBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            findNavController().navigateUp()
        }, 2000)
    }

}