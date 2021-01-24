package app.mulipati.epoxy.upcoming

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.mulipati.R
import app.mulipati.data.Cancelled
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass(layout = R.layout.model_upcoming)
abstract class UpcomingEpoxyModel : EpoxyModelWithHolder<UpcomingEpoxyModel.UpcomingEpoxyModelViewHolder>() {

    @EpoxyAttribute
    var data: Cancelled? = null

    @EpoxyAttribute
    var click: View.OnClickListener? = null

    override fun createNewHolder(): UpcomingEpoxyModelViewHolder {
        return UpcomingEpoxyModelViewHolder()
    }

    override fun bind(holder: UpcomingEpoxyModelViewHolder) {
        super.bind(holder)

        holder.title!!.text = data!!.title
        holder.datetime!!.text = data!!.datetime

        holder.menu!!.setOnClickListener(click)
        holder.chat!!.setOnClickListener(click)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.model_upcoming
    }

    class UpcomingEpoxyModelViewHolder : EpoxyHolder() {

        var title: TextView? = null
        var datetime: TextView? = null
        var chat: ImageView? = null

        var menu: ImageView? = null

        override fun bindView(itemView: View) {

            title = itemView.findViewById(R.id.cancelledTitle)
            datetime = itemView.findViewById(R.id.cancelledDate)

            menu = itemView.findViewById(R.id.cancelledMenu)
            chat = itemView.findViewById(R.id.toChat)

        }

    }
}