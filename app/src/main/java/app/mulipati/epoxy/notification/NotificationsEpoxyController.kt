package app.mulipati.epoxy.notification

import android.widget.PopupMenu
import android.widget.Toast
import app.mulipati.R
import app.mulipati.data.Notifications
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.network.responses.Basic
import com.airbnb.epoxy.Typed2EpoxyController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
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

                                        val apiClient = ApiClient.client!!.create(Routes::class.java)
                                        val markAsRead = apiClient.markAsRead(parentView.id)

                                        markAsRead?.enqueue(object : Callback<Basic?>{
                                            override fun onFailure(call: Call<Basic?>, t: Throwable) {
                                                Toast.makeText(
                                                        parentView.content?.context, "A network error occurred", Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            override fun onResponse(
                                                    call: Call<Basic?>,
                                                    response: Response<Basic?>
                                            ) {
                                                when(response.code()){
                                                    200 ->{
                                                        Toast.makeText(
                                                                parentView.content?.context, "Marked as read", Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }
                                            }

                                        })
                                    }

                                    R.id.delete -> {

                                        val apiClient = ApiClient.client!!.create(Routes::class.java)
                                        val delete = apiClient.deleteNotification(parentView.id)

                                        Timber.e(parentView.id.toString())

                                        delete?.enqueue(object : Callback<Basic?> {
                                            override fun onFailure(call: Call<Basic?>, t: Throwable) {
                                                Toast.makeText(
                                                        parentView.content?.context, "A network error occurred", Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            override fun onResponse(
                                                    call: Call<Basic?>,
                                                    response: Response<Basic?>
                                            ) {
                                                when(response.code()){
                                                    200 ->{
                                                        Toast.makeText(
                                                                parentView.content?.context, "Deleted", Toast.LENGTH_SHORT
                                                        ).show()
                                                    }else->{
                                                    Timber.e(response.errorBody()?.string())
                                                }
                                                }
                                            }

                                        })
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