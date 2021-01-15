package app.mulipati.ui.trips.cancelled

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.mulipati.R
import app.mulipati.data.Cancelled
import app.mulipati.databinding.FragmentCancelledBinding
import app.mulipati.epoxy.cancelled.CancelledEpoxyController


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cancelled = ArrayList<Cancelled>()
        cancelled.add(Cancelled("Blantyre - Zomba", "11 Feb, 2021 - 12:50 PM"))
        cancelled.add(Cancelled("Limbe - Ndata", "12 Feb, 2021 - 6:20 AM"))
        cancelled.add(Cancelled("Kanjedza - CFAO, Mandala", "15 Feb, 2021 - 8:11 AM"))
        cancelled.add(Cancelled("Lunzu - Limbe", "18 Feb, 2021 - 15:09 PM"))
        cancelled.add(Cancelled("Chichiri - Chirobwe", "20 Feb, 2021 - 21:00 PM"))

        controller = CancelledEpoxyController()
        controller.setData(true, cancelled)

        cancelledBinding.cancelledRecycler.setController(controller)
    }
}