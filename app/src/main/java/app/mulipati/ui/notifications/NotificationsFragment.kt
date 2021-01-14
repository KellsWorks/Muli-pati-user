package app.mulipati.ui.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.mulipati.R
import app.mulipati.data.Notifications
import app.mulipati.databinding.FragmentNotificationsBinding
import app.mulipati.epoxy.notification.NotificationsEpoxyController

class NotificationsFragment : Fragment() {

    private lateinit var notificationsBinding: FragmentNotificationsBinding
    private lateinit var controller: NotificationsEpoxyController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        notificationsBinding = FragmentNotificationsBinding.inflate(inflater, container, false)
        notificationsBinding.lifecycleOwner = this

        return notificationsBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val notifications = ArrayList<Notifications>()

        notifications.add(Notifications(R.drawable.ic_bell_ring, "Booking successful!", "You have booked a trip on 2 February 2021 at 4pm", "4 Feb, 2021 11:45AM"))
        notifications.add(Notifications(R.drawable.ic_bell_ring, "Booking successful!", "You have booked a trip on 2 February 2021 at 4pm", "4 Feb, 2021 11:45AM"))

        controller = NotificationsEpoxyController()
        controller.setData(true, notifications)

        notificationsBinding.recentNotificationsRecycler
            .setController(controller)

        val old = ArrayList<Notifications>()

        old.add(Notifications(R.drawable.ic_bell_sleep, "Booking successful!", "You have booked a trip on 2 February 2021 at 4pm", "4 Feb, 2021 11:45AM"))
        old.add(Notifications(R.drawable.ic_bell_sleep, "Booking successful!", "You have booked a trip on 2 February 2021 at 4pm", "4 Feb, 2021 11:45AM"))

        val controllerOne = NotificationsEpoxyController()
        controllerOne.setData(true, old)

        notificationsBinding.viewedNotificationsRecycler
            .setController(controllerOne)

    }
}