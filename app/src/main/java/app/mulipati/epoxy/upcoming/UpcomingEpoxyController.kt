package app.mulipati.epoxy.upcoming

import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.findNavController
import app.mulipati.R
import app.mulipati.data.Cancelled
import com.airbnb.epoxy.Typed2EpoxyController
import java.lang.reflect.Method

class UpcomingEpoxyController: Typed2EpoxyController<Boolean?, List<Cancelled>>() {
    override fun buildModels(status: Boolean?, cancelled: List<Cancelled>?) {
        if (cancelled != null) {
            for (cancel in cancelled){
                UpcomingEpoxyModel_()
                        .id(cancel.title)
                        .data(cancel)
                        .click { _, parentView, _, _ ->
                            val popupMenu = PopupMenu(parentView.menu!!.context, parentView.menu)

                            popupMenu.menuInflater.inflate(R.menu.upcoming, popupMenu.menu)
                            popupMenu.setOnMenuItemClickListener { item ->
                                when(item.itemId) {

                                    R.id.delete -> {
                                        Toast.makeText(parentView.datetime!!.context, "Deleted!", Toast.LENGTH_SHORT)
                                                .show()
                                    }

                                    R.id.enterChat->{
                                        parentView.datetime!!.findNavController().navigate(R.id.action_tripsFragment_to_tripChatFragment)
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