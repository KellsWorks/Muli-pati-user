package app.mulipati.epoxy.notification

import android.content.Context
import android.view.LayoutInflater
import android.widget.PopupMenu
import android.widget.PopupWindow
import app.mulipati.R
import app.mulipati.data.Notifications
import com.airbnb.epoxy.Typed2EpoxyController


class NotificationsEpoxyController: Typed2EpoxyController<Boolean?, List<Notifications>>() {
    override fun buildModels(status: Boolean?, notifications: List<Notifications>?) {
        if (notifications != null) {
            for (notification in notifications){
                NotificationsEpoxyModel_()
                    .id(notification.title)
                    .data(notification)
                    .click { _, parentView, _, _ ->
                        val popupMenu = PopupMenu(parentView.menu!!.context, parentView.content)

                        popupMenu.menuInflater.inflate(R.menu.notifications, popupMenu.menu)
                        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                            when(item.itemId) {
                                R.id.asRead -> {}

                                R.id.delete -> {}
                            }
                            true
                        })

                        popupMenu.show()
                    }
                    .addTo(this)
            }
        }
    }
}