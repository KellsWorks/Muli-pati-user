package app.mulipati.ui.trips.completed

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import app.mulipati.data.Completed
import app.mulipati.databinding.FragmentCompletedBinding
import app.mulipati.epoxy.completed.CompletedEpoxyController
import app.mulipati.ui.trips.upcoming.UpcomingViewModel
import app.mulipati.util.Resource
import app.mulipati.util.autoCleared
import app.mulipati.view_models.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class CompletedFragment : Fragment() {

    private var completedBinding: FragmentCompletedBinding by autoCleared()

    private lateinit var controller: CompletedEpoxyController

    private val upcomingViewModel: UpcomingViewModel by viewModels()

    private val usersViewModel : UsersViewModel by viewModels()

    private lateinit var completedList: ArrayList<Completed>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = CompletedEpoxyController()
        completedList = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        completedBinding = FragmentCompletedBinding.inflate(inflater, container, false)
        completedBinding.lifecycleOwner = this

        return completedBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()

        completedBinding.refreshCancelled.setOnRefreshListener {
            setUpObservers()
        }
    }

    private fun setUpObservers(){
        val getId = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
        upcomingViewModel.bookings.observe(viewLifecycleOwner, Observer {
            completedList.clear()
            when(it.status){
                Resource.Status.LOADING ->{
                    completedBinding.refreshCancelled.isRefreshing = true
                }
                Resource.Status.SUCCESS ->{
                    completedBinding.refreshCancelled.isRefreshing = false
                    try {
                        if (it.data!!.isNotEmpty()){
                            for (book in it.data){
                                if (book.user_id == getId && book.status == "completed"){
                                    getTripDriver(book.user_id)
                                    val name = context?.getSharedPreferences("driver", Context.MODE_PRIVATE)?.getString("name", "")
                                    completedList.add(Completed(book.id,
                                                         book.start + " - " + book.destination,
                                                          name!!,
                                                          book.created_at))
                                }
                            }
                            setUpRecycler(completedList)
                        }else{
                            errorLayout()
                        }
                    }catch (e: Exception){
                        Timber.e(e)
                    }
                }
                Resource.Status.ERROR ->{
                    completedBinding.refreshCancelled.isRefreshing = false
                }
            }
        })
    }

    private fun getTripDriver(id: Int){
        usersViewModel.users.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (it.data!!.isNotEmpty()) {
                        for (users in it.data) {
                            if (users.id == id) {
                                context?.getSharedPreferences("driver", Context.MODE_PRIVATE)?.edit()
                                        ?.putString("name", users.name)
                                        ?.apply()
                            }
                        }
                    }
                }
                Resource.Status.ERROR->{}
                Resource.Status.LOADING->{}
            }
        })
    }

    private fun setUpRecycler(data: List<Completed>) {

        controller.setData(true, data)

        if (data.isNotEmpty()){
            successLayout()
        }else{
            errorLayout()
        }

        completedBinding.completedRecycler.setController(controller)

    }

    private fun errorLayout(){
        completedBinding.errorLayout.visibility = View.VISIBLE
        completedBinding.completedRecycler.visibility = View.GONE
    }

    private fun successLayout(){
        completedBinding.errorLayout.visibility = View.GONE
        completedBinding.completedRecycler.visibility = View.VISIBLE
    }
}