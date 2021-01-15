package app.mulipati.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.databinding.FragmentTripBinding


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tripBinding.tripBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}