package com.dicoding.githubuser.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.detail.follow.FollowsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra(EXTRA_USERNAME) ?: ""


        viewModel.users.observe(this){
            if (it != null){
                binding.apply {
                    nama.text = it.login
                    name.text = it.name
                    followerCount.text = it.followers.toString()
                    followingCount.text = it.following.toString()
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .into(image)
                }
            }
        }
        viewModel.setUserDetail(username)

        val followersFragment = FollowsFragment.newInstance(FollowsFragment.FOLLOWERS)
        val followingFragment = FollowsFragment.newInstance(FollowsFragment.FOLLOWING)


        followersFragment.setUsername(username)
        followingFragment.setUsername(username)

        val fragments = mutableListOf<Fragment>(
            followersFragment,
            followingFragment
        )

        val titleFragments = mutableListOf(
            getString(R.string.follower), getString(R.string.following)
        )

        val adapter = SectionsPagerAdapter(this, fragments)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tab, binding.viewpager) { tab, posisi ->
            tab.text = titleFragments[posisi]
        }.attach()

        viewModel.loadFollowers(username)
        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0 && viewModel.followers.value == null){
                    viewModel.loadFollowers(username)
                } else if (tab?.position == 1 && viewModel.followings.value == null) {
                    viewModel.loadFollowings(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
}