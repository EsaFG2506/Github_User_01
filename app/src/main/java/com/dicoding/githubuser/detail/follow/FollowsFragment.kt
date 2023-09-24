package com.dicoding.githubuser.detail.follow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.databinding.FragmentFollowsBinding
import com.dicoding.githubuser.detail.DetailViewModel
import com.dicoding.githubuser.main.UserAdapter

class FollowsFragment : Fragment() {

    private var binding: FragmentFollowsBinding? = null
    private lateinit var adapter: UserAdapter
    private val viewModel by viewModels<DetailViewModel>()

    var type = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentFollowsBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter()

        binding?.rvFollow?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowsFragment.adapter
        }

        when (type) {
            FOLLOWERS -> {
                viewModel.followers.observe(requireActivity()) { followersList ->
                    adapter.submitList(followersList)
                    binding?.rvFollow?.adapter = adapter
                }
            }
            FOLLOWING -> {
                viewModel.followings.observe(requireActivity()) { followingList ->
                    adapter.submitList(followingList)
                    binding?.rvFollow?.adapter = adapter
                }
            }
        }

        viewModel.isLoading.observe(requireActivity()) { isLoading ->
            if (isLoading) {
                binding?.loadFrag?.visibility = View.VISIBLE
            } else {
                binding?.loadFrag?.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (type == FOLLOWERS) {
            viewModel.loadFollowers(username = "esa")
        } else {
            viewModel.loadFollowings(username = "esa")
        }
    }


    companion object {
        const val FOLLOWING = 100
        const val FOLLOWERS = 101

        fun newInstance(type: Int) = FollowsFragment()
            .apply {
                this.type = type
            }
    }

}