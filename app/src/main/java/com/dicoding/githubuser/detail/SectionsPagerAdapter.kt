package com.dicoding.githubuser.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubuser.detail.follow.FollowsFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String = ""

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowsFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowsFragment.ARG_POSITION, position + 1)
            putString(FollowsFragment.ARG_USERNAME, username)
        }
        return fragment
    }

    fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Followers"
            1 -> "Following"
            else -> null
        }
    }
}