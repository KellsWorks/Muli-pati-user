package app.mulipati.ui.trips.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import app.mulipati.data.Cancelled
import app.mulipati.databinding.FragmentUpcomingBinding
import app.mulipati.epoxy.upcoming.UpcomingEpoxyController
import app.mulipati.ui.dashboard.TripsViewModel
import app.mulipati.util.Resource
import app.mulipati.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UpcomingFragment : Fragment() {

    private var upcomingBinding: FragmentUpcomingBinding by autoCleared()

    private lateinit var controller: UpcomingEpoxyController

    private val viewModel: TripsViewModel by viewModels()

//    private val upcomingViewModel: UpcomingViewModel by viewModels()

    private lateinit var tripsList: ArrayList<Cancelled>


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
        tripsList = ArrayList()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()

    }
    private fun errorLayout(){
        upcomingBinding.errorLayout.visibility = View.VISIBLE
        upcomingBinding.upcomingRecycler.visibility = View.GONE
    }

    private fun successLayout(){
        upcomingBinding.errorLayout.visibility = View.GONE
        upcomingBinding.upcomingRecycler.visibility = View.VISIBLE
    }

    private fun setUpObservers(){


    }

}