@file:Suppress("DEPRECATION")

package app.mulipati.epoxy.completed

import android.app.ProgressDialog
import android.widget.PopupMenu
import android.widget.Toast
import app.mulipati.R
import app.mulipati.data.Completed
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.network.responses.Basic
import com.airbnb.epoxy.Typed2EpoxyController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.reflect.Method


class CompletedEpoxyController: Typed2EpoxyController<Boolean?, List<Completed>>() {
    override fun buildModels(status: Boolean?, completed: List<Completed>?) {
        if (completed != null) {
            for (complete in completed){
                CompletedEpoxyModel_()
                    .id(complete.datetime)
                    .data(complete)
                    .click { _, parentView, _, _ ->
                        val popupMenu = PopupMenu(parentView.menu!!.context, parentView.menu)

                        popupMenu.menuInflater.inflate(R.menu.cancelled, popupMenu.menu)
                        popupMenu.setOnMenuItemClickListener { item ->
                            when(item.itemId) {

                                R.id.delete -> {
                                    val dialog = ProgressDialog(parentView.datetime?.context, R.style.CustomAlertDialog)
                                    dialog.setCancelable(false)
                                    dialog.setMessage("Cancelling trip...")
                                    dialog.show()

                                    val api = ApiClient.client!!.create(Routes::class.java)
                                    val action = api.deleteTrip(parentView.id!!)

                                    action.enqueue(object : Callback<Basic> {
                                        override fun onFailure(call: Call<Basic>, t: Throwable) {
                                            Toast.makeText(parentView.datetime!!.context, "$t", Toast.LENGTH_SHORT)
                                                    .show()
                                            dialog.dismiss()
                                        }


                                        override fun onResponse(call: Call<Basic>, response: Response<Basic>) {
                                            dialog.dismiss()
                                            when(response.code()){
                                                200 ->{
                                                    Toast.makeText(parentView.datetime!!.context, "Trip deleted successfully", Toast.LENGTH_SHORT)
                                                            .show()
                                                }else->{
                                                Timber.e(response.message())}
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