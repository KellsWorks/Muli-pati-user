package app.mulipati.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.mulipati.R
import app.mulipati.data.chat.Messages

class TripChatAdapter(private var arrayList: List<Messages>, toId: Int, fromId: Int, userId: Int) : RecyclerView.Adapter<TripChatAdapter.ViewHolder>() {

    private var messageRight = 1
    private var messageLeft = 0
    private var id = 0

    init {
        messageRight = toId
        messageLeft = fromId
        id = userId
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var message: TextView = itemView.findViewById(R.id.toMsg)
        var time: TextView = itemView.findViewById(R.id.toTime)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == messageRight){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.model_right_chat, parent, false)
            ViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.model_left_chat, parent, false)
            ViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if (arrayList[position].toId == id){
            messageRight
        }else{
            messageLeft
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = arrayList[position]
        holder.message.text = model.from
        holder.time.text = model.fromTime
    }

}