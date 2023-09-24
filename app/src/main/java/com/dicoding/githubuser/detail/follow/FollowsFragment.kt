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
    private var username = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentFollowsBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            type = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME, "")
        }

        adapter = UserAdapter()

        binding?.rvFollow?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowsFragment.adapter
        }

        when (type) {
            FOLLOWERS -> {
                viewModel.loadFollowers(username)
                viewModel.followers.observe(viewLifecycleOwner) { followersList ->
                    adapter.submitList(followersList)
                }
            }
            FOLLOWING -> {
                viewModel.loadFollowings(username)
                viewModel.followings.observe(viewLifecycleOwner) { followingList ->
                    adapter.submitList(followingList)
                }
            }
        }
    }



    companion object {
        const val FOLLOWING = 100
        const val FOLLOWERS = 101
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"


        fun newInstance(type: Int, username: String) = FollowsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_POSITION, type)
                putString(ARG_USERNAME, username)
            }
        }
    }

}