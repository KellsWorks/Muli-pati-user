@file:Suppress("DEPRECATION")

package app.mulipati.epoxy.cancelled

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.widget.PopupMenu
import app.mulipati.R
import app.mulipati.data.Cancelled
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.network.responses.Basic
import com.airbnb.epoxy.Typed2EpoxyController
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.reflect.Method


@SuppressLint("ResourceAsColor")
class CancelledEpoxyController: Typed2EpoxyController<Boolean?, List<Cancelled>>() {
    override fun buildModels(status: Boolean?, cancelled: List<Cancelled>?) {
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
                                    val dialog = ProgressDialog(parentView.title?.context)
                                    dialog.setCancelable(false)
                                    dialog.setMessage("Cancelling trip...")
                                    dialog.show()

                                    val api = ApiClient.client!!.create(Routes::class.java)
                                    val action = api.deleteTrip(parentView.id!!)

                                    action.enqueue(object : Callback<Basic> {
                                        override fun onFailure(call: Call<Basic>, t: Throwable) {
                                            Snackbar.make(parentView.title!!.rootView, "T$t", Snackbar.LENGTH_SHORT)
                                                    .setBackgroundTint(R.color.red)
                                                    .setTextColor(R.color.white)
                                                    .show()
                                            dialog.dismiss()
                                        }


                                        override fun onResponse(call: Call<Basic>, response: Response<Basic>) {
                                            dialog.dismiss()
                                            when(response.code()){
                                                200 ->{
                                                    Snackbar.make(parentView.title!!.rootView, "Trip deleted successfully", Snackbar.LENGTH_SHORT)
                                                            .setBackgroundTint(R.color.red)
                                                            .setTextColor(R.color.white)
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