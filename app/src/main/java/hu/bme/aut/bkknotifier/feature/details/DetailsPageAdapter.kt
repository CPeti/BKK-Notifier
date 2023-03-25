package hu.bme.aut.bkknotifier.feature.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import hu.bme.aut.bkknotifier.feature.details.alert.DetailsAlertFragment
import hu.bme.aut.bkknotifier.feature.details.departure.DetailsDepartureFragment

class DetailsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    companion object {
        private const val NUM_PAGES: Int = 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DetailsDepartureFragment()
            1 -> DetailsAlertFragment()
            else -> DetailsDepartureFragment()
        }
    }

    override fun getItemCount(): Int = NUM_PAGES
}