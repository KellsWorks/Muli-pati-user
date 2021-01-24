package app.mulipati.ui.trips.completed

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.mulipati.R
import app.mulipati.data.Completed
import app.mulipati.databinding.FragmentCompletedBinding
import app.mulipati.epoxy.completed.CompletedEpoxyController


class CompletedFragment : Fragment() {

    private lateinit var completedBinding: FragmentCompletedBinding
    private lateinit var controller: CompletedEpoxyController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        completedBinding = FragmentCompletedBinding.inflate(inflater, container, false)
        completedBinding.lifecycleOwner = this

        return completedBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val completed = ArrayList<Completed>()

        completed.add(Completed("Today, 10:20 AM", R.drawable.mazda_demio, "Blantyre - Zomba", R.drawable.avatar04, "Jerome Msapha"))
        completed.add(Completed("Yesterday, 19:10 AM", R.drawable.mazda_demio, "Blantyre - Kanjedza", R.drawable.avatar04, "Jerome Msapha"))

        controller = CompletedEpoxyController()
        controller.setData(true, completed)

        completedBinding.completedRecycler.setController(controller)

        val completedCount = completed.count()
        val tripsPreferences = context?.getSharedPreferences("trips_count", Context.MODE_PRIVATE)?.edit()

        tripsPreferences?.putString("completedCount", completedCount.toString())
        tripsPreferences?.apply()
    }
}