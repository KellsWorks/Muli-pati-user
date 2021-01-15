package app.mulipati.adapters


import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import app.mulipati.ui.trips.cancelled.CancelledFragment
import app.mulipati.ui.trips.completed.CompletedFragment
import app.mulipati.ui.trips.upcoming.UpcomingFragment


@Suppress("DEPRECATION")
@SuppressLint("Deprecated")
class TabsAdapter(fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager) {

    private val titles = arrayOf("Upcoming", "Completed", "Cancelled")
    override fun getItem(i: Int): Fragment {
        val fragmentUpcoming = UpcomingFragment()
        val fragmentCompleted = CompletedFragment()
        val fragmentCancelled = CancelledFragment()

        val fragments = arrayOf(fragmentUpcoming, fragmentCompleted, fragmentCancelled)

        return fragments[i]

    }

    override fun getPageTitle(position: Int): CharSequence? {

        return titles[position]
    }

    override fun getCount(): Int {
        return 3
    }
}