package app.mulipati.ui.support

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.mulipati.R
import app.mulipati.adapters.TripChatAdapter
import app.mulipati.data.chat.Messages
import app.mulipati.databinding.FragmentSupportBinding
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.network.responses.chats.MessageSent
import app.mulipati.network.responses.chats.MessagesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class SupportFragment : Fragment() {

    private lateinit var supportBinding: FragmentSupportBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        supportBinding = FragmentSupportBinding.inflate(inflater, container, false)
        supportBinding.lifecycleOwner = this

        return supportBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        supportBinding.supportMenu.setOnClickListener {
            val popupMenu = PopupMenu(context, it)

            popupMenu.menuInflater.inflate(R.menu.support, popupMenu.menu)

            popupMenu.show()
        }

        supportBinding.supportBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val arrayList = ArrayList<Messages>()

        val chatID = 1
        val userId = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.getInt("id", 0)

        val chatAdapter = TripChatAdapter(arrayList, userId!!, chatID, userId)
        supportBinding.supportRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }

        loadMessage(arrayList, chatAdapter, userId)

        supportBinding.supportRefresh.setOnRefreshListener {
            loadMessage(arrayList, chatAdapter, userId)
        }

        val lastPosition = chatAdapter.itemCount - 1

        fun sendMessage(message: String){
            if (message.isNotEmpty()){
                sendAMessage(userId, message, getCurrentTime() )
                arrayList.add(Messages(chatID, message, getCurrentTime(), userId))
                chatAdapter.notifyDataSetChanged()
                supportBinding.supportEditor.setText("")
                supportBinding.supportRecycler.scrollToPosition(lastPosition)
            }
        }

        if (supportBinding.supportEditor.text.toString().isNotEmpty()){
            supportBinding.sendMessage.setImageResource(R.drawable.ic_close)
        }else{
            supportBinding.sendMessage.setImageResource(R.drawable.ic_send)
        }

        supportBinding.sendMessage.setOnClickListener {
            supportBinding.supportRecycler.scrollToPosition(lastPosition)
            val message = supportBinding.supportEditor.text
            sendMessage(message.toString())
        }

        supportBinding.supportEditor.setOnEditorActionListener { v, actionId, _ ->
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

    private fun loadMessage(arrayList: ArrayList<Messages>, adapter: TripChatAdapter, userId: Int){

        supportBinding.supportRefresh.isRefreshing = true
        arrayList.clear()

        val apiClient = ApiClient.client!!.create(Routes::class.java)
        val getMessages = apiClient.tripMessages(userId,1)

        getMessages.enqueue(object: Callback<MessagesResponse> {

            override fun onFailure(call: Call<MessagesResponse>, t: Throwable) {
                Timber.e(t)
                supportBinding.supportRefresh.isRefreshing  = false
            }

            override fun onResponse(
                    call: Call<MessagesResponse>,
                    response: Response<MessagesResponse>
            ) {
                supportBinding.supportRefresh.isRefreshing  = false
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

    private fun sendAMessage(fromId: Int, message: String, time: String){
        val apiClient = ApiClient.client!!.create(Routes::class.java)
        val sendMessage = apiClient.sendMessage(1, fromId, message, time)

        sendMessage.enqueue(object : Callback<MessageSent> {
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