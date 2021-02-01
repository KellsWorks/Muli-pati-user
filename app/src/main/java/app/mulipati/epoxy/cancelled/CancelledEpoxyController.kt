package app.mulipati.epoxy.cancelled

import android.widget.PopupMenu
import android.widget.Toast
import app.mulipati.R
import app.mulipati.network.responses.trips.UserTripX
import com.airbnb.epoxy.Typed2EpoxyController
import java.lang.reflect.Method

class CancelledEpoxyController: Typed2EpoxyController<Boolean?, List<UserTripX>>() {
    override fun buildModels(status: Boolean?, cancelled: List<UserTripX>?) {
        if (cancelled != null) {
            for (cancel in cancelled){
                CancelledEpoxyModel_()
                    .id(cancel.id)
                    .data(cancel)
                    .click { _, parentView, _, _ ->
                        val popupMenu = PopupMenu(parentView.menu!!.context, parentView.menu)

                        popupMenu.menuInflater.inflate(R.menu.cancelled, popupMenu.menu)
                        popupMenu.setOnMenuItemClickListener { item ->
                            when(item.itemId) {

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