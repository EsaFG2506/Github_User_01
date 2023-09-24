package com.dicoding.githubuser.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

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

        val sectionPagerAdapter = SectionsPagerAdapter(this)
        val viewPager = binding.viewpager
        viewPager.adapter = sectionPagerAdapter

        val tabLayout = binding.tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = sectionPagerAdapter.getPageTitle(position)
        }.attach()

        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0){
                    viewModel.loadFollowers(username)
                } else {
                    viewModel.loadFollowings(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        viewModel.loadFollowers(username)


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
}