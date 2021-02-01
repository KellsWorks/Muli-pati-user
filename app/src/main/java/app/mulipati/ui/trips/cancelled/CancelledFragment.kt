package app.mulipati.ui.trips.cancelled

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.mulipati.databinding.FragmentCancelledBinding
import app.mulipati.epoxy.cancelled.CancelledEpoxyController
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.network.responses.trips.UpcomingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class CancelledFragment : Fragment() {

    private lateinit var cancelledBinding: FragmentCancelledBinding
    private lateinit var controller: CancelledEpoxyController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        cancelledBinding = FragmentCancelledBinding.inflate(inflater, container, false)
        cancelledBinding.lifecycleOwner = this

       return cancelledBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = CancelledEpoxyController()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancelledBinding.cancelledRecycler.setController(controller)

        val userId = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
        if (userId != null) {
            setUpRecycler(userId)
        }
        cancelledBinding.refreshCancelled.setOnRefreshListener {
            setUpRecycler(userId!!)
        }

    }

    private fun setUpRecycler(userId: Int){

        cancelledBinding.refreshCancelled.isRefreshing = true

        val apiClient = ApiClient.client!!.create(Routes::class.java)
        val getUserTrips: Call<UpcomingResponse> = apiClient.cancelledTrips(userId)

        getUserTrips.enqueue(object : Callback<UpcomingResponse?> {
            override fun onFailure(call: Call<UpcomingResponse?>, t: Throwable) {

                cancelledBinding.errorLayout.visibility = View.VISIBLE
                cancelledBinding.cancelledRecycler.visibility = View.GONE
                cancelledBinding.refreshCancelled.isRefreshing = false

            }

            override fun onResponse(call: Call<UpcomingResponse?>, response: Response<UpcomingResponse?>) {

                cancelledBinding.refreshCancelled.isRefreshing = false

                when(response.code()){
                    200 ->{

                        successLayout()
                        controller.setData(false, response.body()?.userTrips)

                        val cancelledCount = response.body()?.userTrips?.count()
                        val tripsPreferences = context?.getSharedPreferences("trips_count", Context.MODE_PRIVATE)?.edit()

                        tripsPreferences?.putString("cancelledCount", cancelledCount.toString())
                        tripsPreferences?.apply()

                    }else ->{
                    cancelledBinding.errorLayout.visibility = View.VISIBLE
                    cancelledBinding.cancelledRecycler.visibility = View.GONE
                    Timber.e(response.errorBody()?.string())
                }
                }
            }

        })
    }
    private fun successLayout(){
        cancelledBinding.errorLayout.visibility = View.GONE
        cancelledBinding.cancelledRecycler.visibility = View.VISIBLE
    }
}