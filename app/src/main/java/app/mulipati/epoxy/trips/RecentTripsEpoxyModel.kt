package app.mulipati.epoxy.trips

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.mulipati.R
import app.mulipati.network.responses.Trip
import app.mulipati.util.Constants
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide


@EpoxyModelClass(layout = R.layout.model_trips)
abstract class RecentTripsEpoxyModel : EpoxyModelWithHolder<RecentTripsEpoxyModel.RecentTripsEpoxyModelViewHolder>() {

    @EpoxyAttribute
    var data: Trip? = null

    @EpoxyAttribute
    var click: View.OnClickListener? = null

    override fun createNewHolder(): RecentTripsEpoxyModelViewHolder {
        return RecentTripsEpoxyModelViewHolder()
    }

    @SuppressLint("SetTextI18n")
    override fun bind(holder: RecentTripsEpoxyModelViewHolder) {
        super.bind(holder)

        Glide
            .with(holder.image!!.context)
            .load(Constants.CARS_URL + data!!.car_photo)
            .centerCrop()
            .into(holder.image!!)

        holder.title!!.text = data!!.destination + " trip"
        holder.route!!.text = data!!.start + " - " + data!!.destination
        holder.datetime!!.text = data!!.start_time


        holder.mainHolder!!.setOnClickListener(click)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.model_trips
    }

    class RecentTripsEpoxyModelViewHolder : EpoxyHolder() {

        var image: ImageView? = null
        var title: TextView? = null
        var route: TextView? = null
        var datetime: TextView? = null

        var mainHolder: LinearLayout? =  null

        override fun bindView(itemView: View) {

            image = itemView.findViewById(R.id.tripImage)
            title = itemView.findViewById(R.id.tripTitle)
            route = itemView.findViewById(R.id.tripRoute)
            datetime = itemView.findViewById(R.id.tripDateTime)

            mainHolder = itemView.findViewById(R.id.tripMain)
        }

    }
}