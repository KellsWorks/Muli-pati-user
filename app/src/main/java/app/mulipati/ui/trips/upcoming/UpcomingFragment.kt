package app.mulipati.ui.trips.upcoming

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.mulipati.databinding.FragmentUpcomingBinding
import app.mulipati.epoxy.upcoming.UpcomingEpoxyController
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.network.responses.trips.UpcomingResponse
import app.mulipati.network.responses.trips.UserTripX
import app.mulipati.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

@AndroidEntryPoint
class UpcomingFragment : Fragment() {

    private lateinit var upcomingBinding: FragmentUpcomingBinding

    private lateinit var controller: UpcomingEpoxyController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        upcomingBinding = FragmentUpcomingBinding.inflate(inflater, container, false)
        upcomingBinding.lifecycleOwner = this

        return upcomingBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = UpcomingEpoxyController()


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingBinding.upcomingRecycler.setController(
                controller
        )

        val userId = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
        if (userId != null) {
            setUpRecycler(userId)
        }
        upcomingBinding.refreshUpcoming.setOnClickListener {
            if (userId != null) {
                setUpRecycler(userId)
            }else{
                upcomingBinding.refreshUpcoming.isRefreshing = false
            }
        }
    }

    private fun setUpRecycler(userId: Int){

        upcomingBinding.refreshUpcoming.isRefreshing = true

        val apiClient = ApiClient.client!!.create(Routes::class.java)
        val getUserTrips: Call<UpcomingResponse?>? = apiClient.userTrips(userId)

        getUserTrips?.enqueue(object : Callback<UpcomingResponse?>{
            override fun onFailure(call: Call<UpcomingResponse?>, t: Throwable) {

                upcomingBinding.errorLayout.visibility = View.VISIBLE
                upcomingBinding.upcomingRecycler.visibility = View.GONE
                upcomingBinding.refreshUpcoming.isRefreshing = false

            }

            override fun onResponse(call: Call<UpcomingResponse?>, response: Response<UpcomingResponse?>) {

                upcomingBinding.refreshUpcoming.isRefreshing = false

                when(response.code()){
                    200 ->{
                        successLayout()
                        Timber.e(response.body().toString())
                        controller.setData(false, response.body()?.userTrips)
                    }else ->{
                    upcomingBinding.errorLayout.visibility = View.VISIBLE
                    upcomingBinding.upcomingRecycler.visibility = View.GONE
                }
                }
            }

        })
    }


    private fun successLayout(){
        upcomingBinding.errorLayout.visibility = View.GONE
        upcomingBinding.upcomingRecycler.visibility = View.VISIBLE
    }

}