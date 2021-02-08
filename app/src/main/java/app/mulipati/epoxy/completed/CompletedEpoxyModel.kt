package app.mulipati.epoxy.completed

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.mulipati.R
import app.mulipati.data.Completed
import app.mulipati.util.getDisplayDateTime
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder


@SuppressLint("NonConstantResourceId")
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

        holder.datetime!!.text = getDisplayDateTime(data!!.datetime)
        holder.route!!.text = data!!.title
        holder.driverName!!.text = data!!.driverName

        holder.id = data!!.id

        holder.menu!!.setOnClickListener(click)
    }

    override fun getDefaultLayout(): Int {
        return R.layout.model_completed
    }

    class CompletedEpoxyModelViewHolder : EpoxyHolder() {

        var datetime: TextView? = null
        var route: TextView? = null
        var driverName: TextView? = null

        var id: Int? = null

        var menu: ImageView? = null

        override fun bindView(itemView: View) {

            datetime = itemView.findViewById(R.id.completedTime)
            route = itemView.findViewById(R.id.completedRoute)
            driverName = itemView.findViewById(R.id.completedDriverName)

            menu = itemView.findViewById(R.id.completedMenu)

        }

    }
}