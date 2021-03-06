
package app.mulipati.ui.trip

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.mulipati.R
import app.mulipati.adapters.TripChatAdapter
import app.mulipati.data.chat.Messages
import app.mulipati.databinding.FragmentTripChatBinding
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.network.responses.chats.MessageSent
import app.mulipati.network.responses.chats.MessagesResponse
import app.mulipati.ui.dashboard.TripsViewModel
import app.mulipati.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class TripChatFragment : Fragment() {

    private lateinit var tripsChatBinding: FragmentTripChatBinding

    private val tripsViewModel: TripsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        tripsChatBinding = FragmentTripChatBinding.inflate(inflater, container, false)
        tripsChatBinding.lifecycleOwner = this

        return tripsChatBinding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tripsChatBinding.tripChatBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayList = ArrayList<Messages>()

        val tripId = context?.getSharedPreferences("chatPrefs", Context.MODE_PRIVATE)?.getInt("tripId", 0)

        Timber.e(tripId.toString())

        tripsViewModel.trips.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it.status){
                Resource.Status.SUCCESS->{
                    if (it.data!!.isNotEmpty()){
                        for (i in it.data){
                            if (i.id == tripId){
                                context?.getSharedPreferences("chatID", Context.MODE_PRIVATE)?.edit()
                                        ?.putString("title", i.start + " - " + i.destination)
                                        ?.putInt("id", i.user_id)?.apply()
                            }
                        }
                    }
                }
                Resource.Status.LOADING->{}
                Resource.Status.ERROR->{}
            }
        })

        val chatID = context?.getSharedPreferences("chatID", Context.MODE_PRIVATE)?.getInt("id", 0)
        val chatTitle = context?.getSharedPreferences("chatID", Context.MODE_PRIVATE)?.getString("title", "")
        val userId = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)

        tripsChatBinding.chatTitle.text = chatTitle


        val chatAdapter = TripChatAdapter(arrayList, userId!!, chatID!!, userId)
        tripsChatBinding.tripChatRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        loadMessage(arrayList, chatAdapter, userId, chatID)

        tripsChatBinding.refreshChat.setOnRefreshListener {
            loadMessage(arrayList, chatAdapter, userId, chatID)
        }

        val lastPosition = chatAdapter.itemCount - 1

        fun sendMessage(message: String){
            if (message.isNotEmpty()){

                sendAMessage(userId, message, getCurrentTime(),chatID )
                arrayList.add(Messages(chatID, message, getCurrentTime(), userId))

                chatAdapter.notifyDataSetChanged()

                tripsChatBinding.senderMessage.setText("")
                tripsChatBinding.tripChatRecycler.scrollToPosition(lastPosition)

            }
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
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendMessage(v.toString())
            }
            handled
        }
    }

    private fun getCurrentTime(): String{

        val calendarInstance = Calendar.getInstance()
        val hour = calendarInstance.get(Calendar.HOUR_OF_DAY)
        val minutes = calendarInstance.get(Calendar.MINUTE)

        return "$hour:$minutes"
    }

    private fun loadMessage(arrayList: ArrayList<Messages>, adapter: TripChatAdapter, userId: Int, chatID: Int){

        tripsChatBinding.refreshChat.isRefreshing = true
        arrayList.clear()

        val apiClient = ApiClient.client!!.create(Routes::class.java)
        val getMessages = apiClient.tripMessages(userId,chatID)

        getMessages.enqueue(object: Callback<MessagesResponse>{

            override fun onFailure(call: Call<MessagesResponse>, t: Throwable) {
                Timber.e(t)
                tripsChatBinding.refreshChat.isRefreshing = false
            }

            override fun onResponse(
                    call: Call<MessagesResponse>,
                    response: Response<MessagesResponse>
            ) {
                tripsChatBinding.refreshChat.isRefreshing = false
                when(response.code()){
                    200->{
                        val messages = response.body()?.messages
                        if (messages != null) {
                            for(message in messages){
                                arrayList.add(Messages(message.to, message.message, message.time, message.from  ))
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }

        })
    }

    private fun sendAMessage(fromId: Int, message: String, time: String, toId: Int){

        val apiClient = ApiClient.client!!.create(Routes::class.java)
        val sendMessage = apiClient.sendMessage(toId, fromId, message, time)

        sendMessage.enqueue(object : Callback<MessageSent>{
            override fun onFailure(call: Call<MessageSent>, t: Throwable) {
                Toast.makeText(requireContext(), "A network error occurred", Toast.LENGTH_SHORT)
                        .show()
            }

            override fun onResponse(call: Call<MessageSent>, response: Response<MessageSent>) {
                when(response.code()){
                    200 -> {
                        Toast.makeText(requireContext(), "Message Sent!", Toast.LENGTH_SHORT)
                                .show()
                    }
                    else ->{
                        Toast.makeText(requireContext(), "Message not sent!", Toast.LENGTH_SHORT)
                                .show()
                    }
                }
            }

        })
    }
}