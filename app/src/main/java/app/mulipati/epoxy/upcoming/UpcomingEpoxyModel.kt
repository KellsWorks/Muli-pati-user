package app.mulipati.epoxy.upcoming

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.mulipati.R
import app.mulipati.network.responses.trips.UserTripX
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass(layout = R.layout.model_upcoming)
abstract class UpcomingEpoxyModel : EpoxyModelWithHolder<UpcomingEpoxyModel.UpcomingEpoxyModelViewHolder>() {

    @EpoxyAttribute
    var data: UserTripX? = null

    @EpoxyAttribute
    var click: View.OnClickListener? = null

    override fun createNewHolder(): UpcomingEpoxyModelViewHolder {
        return UpcomingEpoxyModelViewHolder()
    }

    @SuppressLint("SetTextI18n")
    override fun bind(holder: UpcomingEpoxyModelViewHolder) {
        super.bind(holder)

        holder.title!!.text = data!!.start + " - " + data!!.destination
        holder.datetime!!.text = data!!.start_time

        holder.menu!!.setOnClickListener(click)
        holder.id = data!!.id

    }

    override fun getDefaultLayout(): Int {
        return R.layout.model_upcoming
    }

    class UpcomingEpoxyModelViewHolder : EpoxyHolder() {

        var title: TextView? = null
        var datetime: TextView? = null
        var menu: ImageView? = null

        var id: Int? = null

        override fun bindView(itemView: View) {

            title = itemView.findViewById(R.id.cancelledTitle)
            datetime = itemView.findViewById(R.id.cancelledDate)

            menu = itemView.findViewById(R.id.cancelledMenu)

        }

    }
}