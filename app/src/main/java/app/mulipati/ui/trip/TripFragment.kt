package app.mulipati.ui.trip

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentTripBinding
import app.mulipati.util.Constants
import com.bumptech.glide.Glide


class TripFragment : Fragment() {

    private lateinit var tripBinding: FragmentTripBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        tripBinding = FragmentTripBinding.inflate(inflater, container, false)
        tripBinding.lifecycleOwner = this

        return tripBinding.root
    }

    @SuppressLint("SetTextI18n")
    private fun bindTripDetails(){

        val details = context?.getSharedPreferences("trip_details", Context.MODE_PRIVATE)

            Glide
                .with(tripBinding.tripCarPhoto)
                .load(Constants.CARS_URL + details?.getString("car_photo", ""))
                .centerCrop()
                .into(tripBinding.tripCarPhoto)

            tripBinding.tripSeats.text = "Total seats: " + details?.getString("number_of_passengers", "") + " |" + details?.getString("number_of_passengers", "") + " Available"
            tripBinding.tripStartTime.text = details?.getString("start_time", "")
            tripBinding.tripFare.text = "MK" + details?.getString("passenger_fare", "")
            tripBinding.pickUpPlace.text  = details?.getString("pick_up_place", "")
            tripBinding.tripSummary.text = "This trip is from Blantyre to Zomba. It will start at... to..."
            tripBinding.tripTitleText.text = details?.getString("start", "") + " - " + details?.getString("destination", "") + " trip"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindTripDetails()

        tripBinding.tripBack.setOnClickListener {
            findNavController().navigate(R.id.action_tripFragment_to_dashboardFragment)
        }

        tripBinding.bookTrip.setOnClickListener {
            findNavController().navigate(R.id.action_tripFragment_to_bookingSuccess)
        }
    }
}