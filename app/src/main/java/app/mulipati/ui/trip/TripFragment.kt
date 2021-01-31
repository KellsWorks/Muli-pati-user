@file:Suppress("DEPRECATION")

package app.mulipati.ui.trip

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentTripBinding
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.network.responses.trips.BookingResponse
import app.mulipati.util.Constants
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


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

        val tripId = context?.getSharedPreferences("trip_details", Context.MODE_PRIVATE)!!.getInt("id", 0)
        val userId = context?.getSharedPreferences("user", Context.MODE_PRIVATE)!!.getInt("id", 0)

        tripBinding.tripBack.setOnClickListener {
            findNavController().navigate(R.id.action_tripFragment_to_dashboardFragment)
        }
        
        tripBinding.bookTrip.setOnClickListener {
            bookTrip(
                    userId, tripId
            )
        }
    }

    private fun bookTrip(bookId: Int, tripId: Int){

        val apiClient = ApiClient.client!!.create(Routes::class.java)
        val book: Call<BookingResponse> = apiClient.bookTrip(bookId, tripId)

        val dialog = ProgressDialog(requireContext())
        dialog.setCancelable(false)
        dialog.setMessage("Booking trip...")
        dialog.show()

        book.enqueue(object : Callback<BookingResponse?> {

            override fun onFailure(call: Call<BookingResponse?>, t: Throwable) {
                Timber.e(t)
                dialog.dismiss()
                Toast.makeText(requireContext(), "A network error occurred", Toast.LENGTH_SHORT)
                        .show()
            }

            override fun onResponse(call: Call<BookingResponse?>, response: Response<BookingResponse?>) {

                if (response.isSuccessful) {
                    when(response.code()){
                        200 -> {
                            findNavController().navigate(R.id.action_tripFragment_to_bookingSuccess)
                            dialog.dismiss()

                        }
                        202 -> {
                            dialog.dismiss()
                            val dialogClickListener = DialogInterface.OnClickListener { _, which ->
                                when (which) {
                                    DialogInterface.BUTTON_NEGATIVE -> {

                                    }

                                }
                            }

                            val alertDialog = AlertDialog.Builder(requireContext())
                                    .setTitle("Disclaimer")
                                    .setMessage(response.body()?.message)
                                    .setIcon(R.drawable.ic_warning)
                                    .setNegativeButton("Dismiss", dialogClickListener)
                            alertDialog.show()

                        }
                    }
                }else {
                    Timber.e(response.code().toString())
                    Timber.e(response.errorBody()?.string())

                }

            }

        })
    }
}