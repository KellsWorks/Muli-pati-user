@file:Suppress("DEPRECATION")

package app.mulipati.epoxy.upcoming

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.findNavController
import app.mulipati.R
import app.mulipati.data.chat.Upcoming
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.network.responses.trips.CancelResponse
import com.airbnb.epoxy.Typed2EpoxyController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.lang.reflect.Method


@SuppressLint("ResourceAsColor")
class UpcomingEpoxyController : Typed2EpoxyController<Boolean, List<Upcoming>>() {
    override fun buildModels(status: Boolean?, bookings: List<Upcoming>?) {
        if (bookings != null) {
            for (book in bookings){
                UpcomingEpoxyModel_()
                        .id(book.id)
                        .data(book)
                        .click { _, parentView, _, _ ->

                            val popupMenu = PopupMenu(parentView.menu!!.context, parentView.menu)
                            val userId = parentView.title?.context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)


                            val tripId = parentView.id

                            Timber.e("User id: $userId Trip id: $tripId")


                            popupMenu.menuInflater.inflate(R.menu.upcoming, popupMenu.menu)
                            popupMenu.setOnMenuItemClickListener { item ->
                                when(item.itemId) {

                                    R.id.delete -> {

                                        val dialog = ProgressDialog(parentView.title?.context, R.style.CustomAlertDialog)
                                        dialog.setCancelable(false)
                                        dialog.setMessage("Cancelling trip...")
                                        dialog.show()

                                        val api = ApiClient.client!!.create(Routes::class.java)
                                        val action = api.cancelTrip(userId, parentView.id)

                                        action.enqueue(object : Callback<CancelResponse> {
                                            override fun onFailure(call: Call<CancelResponse>, t: Throwable) {
                                                Toast.makeText(parentView.title!!.context, "$t", Toast.LENGTH_SHORT)
                                                        .show()
                                                dialog.dismiss()
                                            }

                                            override fun onResponse(call: Call<CancelResponse>, response: Response<CancelResponse>) {
                                                dialog.dismiss()
                                                when(response.code()){
                                                    200 ->{
                                                        Toast.makeText(parentView.title!!.context, "Trip cancelled successfully", Toast.LENGTH_SHORT)
                                                                .show()
                                                    }else->{
                                                    Timber.e(response.message())}
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