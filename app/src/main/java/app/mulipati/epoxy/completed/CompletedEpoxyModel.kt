package app.mulipati.epoxy.completed

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.mulipati.R
import app.mulipati.data.Completed
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@EpoxyModelClass(layout = R.layout.model_completed)
abstract class CompletedEpoxyModel : EpoxyModelWithHolder<CompletedEpoxyModel.CompletedEpoxyModelViewHolder>() {

    @EpoxyAttribute
    var data: Completed? = null

    @EpoxyAttribute
    var click: View.OnClickListener? = null

    override fun createNewHolder(): CompletedEpoxyModelViewHolder {
        return CompletedEpoxyModelViewHolder()
    }

    override fun bind(holder: CompletedEpoxyModelViewHolder) {
        super.bind(holder)

        holder.datetime!!.text = data!!.datetime
        holder.image!!.setImageResource(data!!.image)
        holder.route!!.text = data!!.route
        holder.driverIcon!!.setImageResource(data!!.drivericon)
        holder.driverName!!.text = data!!.drivername

        holder.menu!!.setOnClickListener(click)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.model_completed
    }

    class CompletedEpoxyModelViewHolder : EpoxyHolder() {

        var datetime: TextView? = null
        var image: ImageView? = null
        var route: TextView? = null
        var driverIcon: de.hdodenhof.circleimageview.CircleImageView? = null
        var driverName: TextView? = null


        var menu: ImageView? = null

        override fun bindView(itemView: View) {

            datetime = itemView.findViewById(R.id.completedTime)
            image = itemView.findViewById(R.id.completedImage)
            route = itemView.findViewById(R.id.completedRoute)
            driverIcon = itemView.findViewById(R.id.completedDriverIcon)
            driverName = itemView.findViewById(R.id.completedDriverName)

            menu = itemView.findViewById(R.id.completedMenu)

        }

    }
}