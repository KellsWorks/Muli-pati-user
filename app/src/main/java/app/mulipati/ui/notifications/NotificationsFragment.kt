package app.mulipati.ui.notifications

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentNotificationsBinding
import app.mulipati.epoxy.notification.NotificationsEpoxyController
import app.mulipati.util.Resource
import app.mulipati.util.autoCleared
import app.mulipati.data.Notifications
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var notificationsBinding: FragmentNotificationsBinding by  autoCleared()

    private lateinit var controller: NotificationsEpoxyController

    private val notificationsViewModel: NotificationsViewModel by viewModels()

    private lateinit var notificationsList: ArrayList<Notifications>

    private lateinit var viewedNotificationsList: ArrayList<Notifications>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        notificationsBinding = FragmentNotificationsBinding.inflate(inflater, container, false)
        notificationsBinding.lifecycleOwner = this

        return notificationsBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationsBinding.notificationsBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }


    private fun setupObservers() {
        val getId = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
        notificationsViewModel.notications.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    try {
                        if (it.data!!.isNotEmpty() ){
                            notificationsList = ArrayList()
                            viewedNotificationsList = ArrayList()
                            for (notify in it.data){
                                if (notify.user_id == getId && notify.status == "unmarked"
                                ) {
                                    notificationsList.clear()
                                    notificationsList.add(
                                        Notifications(
                                            notify.id, R.drawable.ic_bell_ring, notify.title, notify.content, notify.created_at
                                        )
                                    )

                                }
                                else if(notify.user_id == getId && notify.status == "marked"){
                                    viewedNotificationsList.clear()
                                    viewedNotificationsList.add(
                                        Notifications(
                                            notify.id, R.drawable.ic_bell_sleep, notify.title, notify.content, notify.created_at
                                        )
                                    )
                                }

                            }
                            setUpRecycler(notificationsList)
                            setUpSecondRecycler(viewedNotificationsList)
                        }
                    }catch (e: IndexOutOfBoundsException){
                        Timber.e(e)
                    }
                }

                Resource.Status.ERROR ->
                    Timber.e(it.message)

                Resource.Status.LOADING -> {

                }
            }
        })
    }

    private fun setUpRecycler(data: List<Notifications>) {

        controller = NotificationsEpoxyController()
        controller.setData(true, data)

        if (data.isNotEmpty()){
            notificationsBinding.recentNotificationsRecycler.visibility = View.VISIBLE
            notificationsBinding.errorFirstLayout.visibility = View.GONE
        }else{
            notificationsBinding.recentNotificationsRecycler.visibility = View.GONE
            notificationsBinding.errorFirstLayout.visibility = View.VISIBLE
        }

        notificationsBinding.recentNotificationsRecycler
            .setController(controller)

    }

    private fun setUpSecondRecycler(data: List<Notifications>) {

        controller = NotificationsEpoxyController()
        controller.setData(true, data)

        if (data.isNotEmpty()){
            notificationsBinding.viewedNotificationsRecycler.visibility = View.VISIBLE
            notificationsBinding.errorSecondLayout.visibility = View.GONE
        }else{
            notificationsBinding.viewedNotificationsRecycler.visibility = View.GONE
            notificationsBinding.errorSecondLayout.visibility = View.VISIBLE
        }

        notificationsBinding.viewedNotificationsRecycler
            .setController(controller)

    }

}