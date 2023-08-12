package com.sample.mainapplication.ui.first

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sample.mainapplication.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstFragment : Fragment() {

    private lateinit var mainRecyclerView : RecyclerView
    private lateinit var mainSearchView: SearchView
    private lateinit var firstRecyclerViewAdapter : FirstRecyclerViewAdapter
    private val viewModel by viewModels<FirstFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMainDatListObserver()
        setNavBar()
        setRecyclerView(view)
        setSearchView(view)
    }

    private fun setRecyclerView(view: View) {
        mainRecyclerView = view.findViewById(R.id.main_fragment_recyclerview)
        firstRecyclerViewAdapter = FirstRecyclerViewAdapter()

        firstRecyclerViewAdapter.onItemClick = { mainDataName ->
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(mainDataName)
            view.findNavController().navigate(action)
        }
        mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = firstRecyclerViewAdapter
        }
    }

    private fun setMainDatListObserver() {
        viewModel.mainDataList.observe(viewLifecycleOwner) {
            firstRecyclerViewAdapter.updateList(it)
        }
    }

    private fun setNavBar() {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.visibility = View.VISIBLE
    }

    private fun setSearchView(view: View) {
        mainSearchView = view.findViewById(R.id.main_search_view)
        mainSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = viewModel.getFilteredList(newText)
                firstRecyclerViewAdapter.updateList(filteredList)
                return true
            }
        })
    }

    override fun onPause() {
        // closes keyboard which clears Query
        if (!mainSearchView.isIconified) {
            mainSearchView.isIconified = true
        }
        super.onPause()
    }
}