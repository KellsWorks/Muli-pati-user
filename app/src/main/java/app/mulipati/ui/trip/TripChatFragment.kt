package app.mulipati.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.mulipati.R
import app.mulipati.adapters.TripChatAdapter
import app.mulipati.data.chat.Messages
import app.mulipati.databinding.FragmentTripChatBinding
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.network.responses.chats.MessagesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class TripChatFragment : Fragment() {

    private lateinit var tripsChatBinding: FragmentTripChatBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        tripsChatBinding = FragmentTripChatBinding.inflate(inflater, container, false)
        tripsChatBinding.lifecycleOwner = this

        return tripsChatBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayList = ArrayList<Messages>()

        val apiClient = ApiClient.client!!.create(Routes::class.java)
        val getMessages = apiClient.tripMessages(1,2)

        getMessages.enqueue(object: Callback<MessagesResponse>{
            override fun onFailure(call: Call<MessagesResponse>, t: Throwable) {
                Timber.e(t)
            }

            override fun onResponse(
                call: Call<MessagesResponse>,
                response: Response<MessagesResponse>
            ) {
                when(response.code()){
                    200->{
                        val messages = response.body()?.messages
                        if (messages != null) {
                            for(message in messages){
                                arrayList.add(Messages(message.from, message.message, message.created_at, message.to  ))
                            }
                        }
                    }
                }
            }

        })
        val adpater = TripChatAdapter(arrayList, requireContext(), 27, 12)
        tripsChatBinding.tripChatRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adpater
        }

        val lastPosition = adpater.itemCount - 1

        fun sendMessage(message: String){
            arrayList.add(Messages(1, message, getCurrentTime(), 2))
            adpater.notifyDataSetChanged()
            tripsChatBinding.senderMessage.setText("")
            tripsChatBinding.tripChatRecycler.scrollToPosition(lastPosition)
        }

        if (tripsChatBinding.senderMessage.text.toString().isNotEmpty()){
            tripsChatBinding.sendTripMessage.setImageResource(R.drawable.ic_close)
        }else{
            tripsChatBinding.sendTripMessage.setImageResource(R.drawable.ic_send)
        }

        tripsChatBinding.sendTripMessage.setOnClickListener {
            tripsChatBinding.tripChatRecycler.scrollToPosition(lastPosition)
            val message = tripsChatBinding.senderMessage.text
            sendMessage(message.toString())
        }

        tripsChatBinding.senderMessage.setOnEditorActionListener { v, actionId, _ ->
            val handled = false
            if (actionId == EditorInfo.IME_ACTION_GO) {
                sendMessage(v.toString())
            }
            handled
        }
    }

    private fun getEmojiByUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }

    private fun getCurrentTime(): String{

        val calendarInstance = Calendar.getInstance()
        val hour = calendarInstance.get(Calendar.HOUR_OF_DAY)
        val minutes = calendarInstance.get(Calendar.MINUTE)

        return "$hour:$minutes"
    }
}