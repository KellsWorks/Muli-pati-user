package app.mulipati.epoxy.notification

import android.widget.PopupMenu
import android.widget.Toast
import app.mulipati.R
import app.mulipati.data.Notifications
import com.airbnb.epoxy.Typed2EpoxyController
import java.lang.reflect.Method


class NotificationsEpoxyController: Typed2EpoxyController<Boolean?, List<Notifications>>() {
    override fun buildModels(status: Boolean?, notifications: List<Notifications>?) {
        if (notifications != null) {
            for (notification in notifications){
                NotificationsEpoxyModel_()
                    .id(notification.title)
                    .data(notification)
                    .click { _, parentView, _, _ ->
                        val popupMenu = PopupMenu(parentView.menu!!.context, parentView.menu)

                        popupMenu.menuInflater.inflate(R.menu.notifications, popupMenu.menu)
                        popupMenu.setOnMenuItemClickListener { item ->
                            when(item.itemId) {
                                R.id.asRead -> {
                                    Toast.makeText(parentView.datetime!!.context, "Marked as read!", Toast.LENGTH_SHORT)
                                        .show()
                                }

                                R.id.delete -> {
                                    Toast.makeText(parentView.datetime!!.context, "Deleted!", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                            true
                        }

                        try {
                            val classPopupMenu = Class.forName(
                                popupMenu
                                    .javaClass.name
                            )
                            val mPopup =
                                classPopupMenu.getDeclaredField("mPopup")
                            mPopup.isAccessible = true
                            val menuPopupHelper = mPopup[popupMenu]
                            val classPopupHelper = Class.forName(
                                menuPopupHelper
                                    .javaClass.name
                            )
                            val setForceIcons: Method = classPopupHelper.getMethod(
                                "setForceShowIcon", Boolean::class.javaPrimitiveType
                            )
                            setForceIcons.invoke(menuPopupHelper, true)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        popupMenu.show()
                    }
                    .addTo(this)
            }
        }
    }
}