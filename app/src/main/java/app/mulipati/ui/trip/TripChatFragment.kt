package app.mulipati.ui.trip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.mulipati.adapters.TripChatAdapter
import app.mulipati.data.chat.Messages
import app.mulipati.databinding.FragmentTripChatBinding


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

        val unicode = 0x1F60A
        val emoji = getEmojiByUnicode(unicode)

        arrayList.add(Messages(27, "Hello there", "12:00PM", 12, "What's up", "12:03PM"))
        arrayList.add(Messages(12, "hello", "12:00PM", 27, "What's up", "12:03PM"))
        arrayList.add(Messages(27, "Where will you take me?$emoji", "12:00PM", 27, "What's up", "12:03PM"))
        arrayList.add(Messages(12, "Shoprite bwa?", "12:00PM", 12, "What's up", "12:03PM"))
        arrayList.add(Messages(27, "That's okay then, time yanji?", "12:00PM", 27, "What's up", "12:03PM"))

        val adpater = TripChatAdapter(arrayList, requireContext(), 27, 12)
        tripsChatBinding.tripChatRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adpater
        }

        fun sendMessage(message: String){
            arrayList.add(Messages(27, message, "12:00PM", 27, "What's up", "12:03PM"))
            adpater.notifyDataSetChanged()
            tripsChatBinding.senderMessage.setText("")
        }

        tripsChatBinding.sendTripMessage.setOnClickListener {
            tripsChatBinding.tripChatRecycler.scrollToPosition(0)

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
}