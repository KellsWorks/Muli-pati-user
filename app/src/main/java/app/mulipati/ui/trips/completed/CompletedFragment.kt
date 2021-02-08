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

    }
}