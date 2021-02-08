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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentTripBinding
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.network.responses.trips.BookingResponse
import app.mulipati.ui.trips.upcoming.UpcomingViewModel
import app.mulipati.util.Constants
import app.mulipati.util.Resource
import app.mulipati.view_models.UsersViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

@AndroidEntryPoint
class TripFragment : Fragment() {

    private lateinit var tripBinding: FragmentTripBinding

    private val upcomingViewModel: UpcomingViewModel by viewModels()

    private val usersViewModel : UsersViewModel by viewModels()

    private val bookingsList = ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        tripBinding = FragmentTripBinding.inflate(inflater, container, false)
        tripBinding.lifecycleOwner = this

        return tripBinding.root
    }

    @SuppressLint("SetTextI18n")
    private fun bindTripDetails(){

        val details = context?.getSharedPreferences("trip_details", Context.MODE_PRIVATE)

        val id = details?.getInt("id", 0)
        val userId = details?.getInt("trip_user", 0)
        val start = details?.getString("start", "")
        val destination = details?.getString("destination", "")
        val pickUpPlace = details?.getString("pick_up_place", "")
        val fare = details?.getString("passenger_fare", "")
        val startTime = details?.getString("start_time", "")
        val passengers = details?.getString("number_of_passengers", "")?.toInt()
        val carPhoto = details?.getString("car_photo", "")

        Glide
                .with(tripBinding.tripCarPhoto)
                .load(Constants.CARS_URL + carPhoto)
                .centerCrop()
                .into(tripBinding.tripCarPhoto)

            tripBinding.tripStartTime.text = startTime
            tripBinding.tripFare.text = "MK$fare"
            tripBinding.pickUpPlace.text  = pickUpPlace

            tripBinding.tripTitleText.text = "$start - $destination trip"

            getTripBookings(id!!, passengers!!)
            getTripDriver(userId!!)


            tripBinding.tripSummary.text = "This trip is from $start to $destination. Trip duration will vary depending on the driver. The driver will pick you at $pickUpPlace but it's subject to changes thereby you may talk directly to the driver in the trip chat room."
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindTripDetails()


        tripBinding.tripBack.setOnClickListener {
            findNavController().navigate(R.id.action_tripFragment_to_dashboardFragment)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getTripBookings(id: Int, passengers: Int){

        upcomingViewModel.bookings.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Resource.Status.SUCCESS ->{
                    if (it.data!!.isNotEmpty()){
                        for (bookings in it.data){
                            if (bookings.trip_id == id && bookings.status == "booked"){
                                bookingsList.clear()
                                bookingsList.add(bookings.id.toString())
                            }
                            val bookingsCount = passengers.minus(bookingsList.count())
                            tripBinding.tripSeats.text = "Total seats: $passengers | $bookingsCount Available"

                            val tripId = context?.getSharedPreferences("trip_details", Context.MODE_PRIVATE)!!.getInt("id", 0)
                            val userIdX = context?.getSharedPreferences("user", Context.MODE_PRIVATE)!!.getInt("id", 0)

                            tripBinding.bookTrip.setOnClickListener {
                                if (passengers == bookingsList.count()){
                                    Toast.makeText(requireContext(), "Maximum number of passengers for this trip has been reached", Toast.LENGTH_SHORT)
                                            .show()
                                }else{
                                    bookTrip(userIdX, tripId)
                                }
                            }
                        }
                    }
                }
                Resource.Status.ERROR->{}
                Resource.Status.LOADING->{}
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun getTripDriver(id: Int){

        usersViewModel.users.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (it.data!!.isNotEmpty()) {
                        for (users in it.data) {
                            if (users.id == id) {
                                tripBinding.driver.text = users.name
                            }
                        }
                    }
                }
                Resource.Status.ERROR->{}
                Resource.Status.LOADING->{}
            }
        })
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