package com.spell.fotopoc.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.spell.fotopoc.fragments.HomeMapFragment
import com.spell.fotopoc.fragments.HomePOIListFragment

/**
 * Created by Spellbound on 03.04.2017.
 */
class HomePagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    private val NAMES = arrayOf("Map", "List")
    private val COUNT = 2

    override fun getItem(position: Int): Fragment {
        val frag = when (position) {
            0 -> HomeMapFragment()
            1 -> HomePOIListFragment()
            else -> HomePOIListFragment()
        }
        return frag
    }

    override fun getPageTitle(position: Int): CharSequence {
        return NAMES[position]
    }

    override fun getCount(): Int {
        return COUNT
    }

}