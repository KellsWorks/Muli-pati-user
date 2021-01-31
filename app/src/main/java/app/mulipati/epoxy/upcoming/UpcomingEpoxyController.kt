@file:Suppress("DEPRECATION")

package app.mulipati.epoxy.upcoming

import android.app.ProgressDialog
import android.content.Context
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.findNavController
import app.mulipati.R
import app.mulipati.network.ApiClient
import app.mulipati.network.ForegroundServices
import app.mulipati.network.Routes
import app.mulipati.network.responses.trips.BookingResponse
import app.mulipati.network.responses.trips.UserTripX
import com.airbnb.epoxy.Typed2EpoxyController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.reflect.Method

class UpcomingEpoxyController: Typed2EpoxyController<Boolean?, List<UserTripX>>() {
    override fun buildModels(status: Boolean?, cancelled: List<UserTripX>?) {
        if (cancelled != null) {
            for (cancel in cancelled){
                UpcomingEpoxyModel_()
                        .id(cancel.id)
                        .data(cancel)
                        .click { _, parentView, _, _ ->
                            val popupMenu = PopupMenu(parentView.menu!!.context, parentView.menu)

                            val userId = parentView.title?.context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
                            ForegroundServices().userBooking(parentView.id!!, userId!!)
                            val parsedID = parentView.title?.context?.getSharedPreferences("parsedID", Context.MODE_PRIVATE)?.getInt("id", 0)

                            popupMenu.menuInflater.inflate(R.menu.upcoming, popupMenu.menu)
                            popupMenu.setOnMenuItemClickListener { item ->
                                when(item.itemId) {

                                    R.id.delete -> {
                                        val dialog = ProgressDialog(parentView.title?.context)
                                        dialog.setCancelable(false)
                                        dialog.setMessage("Cancelling trip...")
                                        dialog.show()

                                        val api = ApiClient.client!!.create(Routes::class.java)
                                        val action = api.cancelTrip(parsedID)

                                        Timber.e(parsedID.toString())

                                        action.enqueue(object : Callback<BookingResponse>{
                                            override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                                                Timber.e(t)
                                                dialog.dismiss()
                                            }

                                            override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                                                dialog.dismiss()
                                                when(response.code()){
                                                    200 ->{
                                                        Toast.makeText(parentView.title?.context, "Trip cancelled successfully", Toast.LENGTH_SHORT)
                                                                .show()
                                                    }else->{Timber.e(response.message())}
                                                }
                                            }

                                        })
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