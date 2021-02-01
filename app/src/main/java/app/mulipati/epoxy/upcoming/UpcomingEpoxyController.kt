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
import app.mulipati.network.responses.trips.CancelResponse
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
                        .click { model, parentView, _, _ ->
                            val popupMenu = PopupMenu(parentView.menu!!.context, parentView.menu)

                            val userId = parentView.title?.context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)
                            ForegroundServices(parentView.title?.context!!).userBooking(userId!!, parentView.id!!)
                            val parsedID = parentView.title?.context?.getSharedPreferences("parsedID", Context.MODE_PRIVATE)?.getInt("id", 0)

                            val chatID = parentView.title?.context?.getSharedPreferences("chatID", Context.MODE_PRIVATE)?.edit()
                            chatID?.putString("title", parentView.title!!.text.toString())
                            chatID?.putInt("id", model.data!!.user_id)?.apply()


                            Timber.e("the id $parsedID")

                            popupMenu.menuInflater.inflate(R.menu.upcoming, popupMenu.menu)
                            popupMenu.setOnMenuItemClickListener { item ->
                                when(item.itemId) {

                                    R.id.delete -> {
                                        val dialog = ProgressDialog(parentView.title?.context)
                                        dialog.setCancelable(false)
                                        dialog.setMessage("Cancelling trip...")
                                        dialog.show()

                                        val api = ApiClient.client!!.create(Routes::class.java)
                                        val action = api.cancelTrip(userId, parsedID)

                                        action.enqueue(object : Callback<CancelResponse>{
                                            override fun onFailure(call: Call<CancelResponse>, t: Throwable) {
                                                Timber.e(t)
                                                dialog.dismiss()
                                            }

                                            override fun onResponse(call: Call<CancelResponse>, response: Response<CancelResponse>) {
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