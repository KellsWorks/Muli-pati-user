package app.mulipati.ui.trips

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.adapters.TabsAdapter
import app.mulipati.databinding.FragmentTripsBinding


class TripsFragment : Fragment() {

    private lateinit var tripsBinding: FragmentTripsBinding

    private lateinit var adapter: TabsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        tripsBinding = FragmentTripsBinding
            .inflate(inflater, container, false)
        tripsBinding
            .lifecycleOwner = this

        return  tripsBinding
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TabsAdapter(childFragmentManager)
        tripsBinding.tripsViewPager.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tripsBinding.tripsBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}