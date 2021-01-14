package app.mulipati.epoxy.notification

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.mulipati.R
import app.mulipati.data.Notifications
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass(layout = R.layout.model_notifications)
abstract class NotificationsEpoxyModel : EpoxyModelWithHolder<NotificationsEpoxyModel.NotificationsEpoxyModelViewHolder>() {

    @EpoxyAttribute
    var data: Notifications? = null

    @EpoxyAttribute
    var click: View.OnClickListener? = null

    override fun createNewHolder(): NotificationsEpoxyModelViewHolder {
        return NotificationsEpoxyModelViewHolder()
    }

    override fun bind(holder: NotificationsEpoxyModelViewHolder) {
        super.bind(holder)

        holder.image!!.setImageResource(data!!.icon)
        holder.title!!.text = data!!.title
        holder.content!!.text = data!!.content
        holder.datetime!!.text = data!!.time

        holder.menu!!.setOnClickListener(click)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.model_notifications
    }

    class NotificationsEpoxyModelViewHolder : EpoxyHolder() {

        var image: ImageView? = null
        var title: TextView? = null
        var content: TextView? = null
        var datetime: TextView? = null

        var menu: ImageView? = null

        override fun bindView(itemView: View) {

            image = itemView.findViewById(R.id.notificationIcon)
            title = itemView.findViewById(R.id.notificationTitle)
            content = itemView.findViewById(R.id.notificationContent)
            datetime = itemView.findViewById(R.id.notificationTime)

            menu = itemView.findViewById(R.id.notificationMenu)

        }

    }
}