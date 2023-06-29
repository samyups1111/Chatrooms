package com.sample.mainapplication.ui.mainfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.mainapplication.ui.mainfragment.MainFragmentDirections
import com.sample.mainapplication.R
import com.sample.mainapplication.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var mainRecyclerView : RecyclerView
    private lateinit var mainSearchView: SearchView
    private lateinit var mainRecyclerViewAdapter : MainRecyclerViewAdapter
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        mainRecyclerView = view.findViewById(R.id.main_fragment_recyclerview)
        mainRecyclerViewAdapter = MainRecyclerViewAdapter()
        mainSearchView = view.findViewById(R.id.main_search_view)
        mainRecyclerViewAdapter.onItemClick = { mainDataName ->
            val action = MainFragmentDirections.actionMainFragmentToSecondFragment(mainDataName)
            view.findNavController().navigate(action)
        }
        mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = mainRecyclerViewAdapter
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.mainDataList.observe(viewLifecycleOwner, Observer {
            mainRecyclerViewAdapter.updateList(it)
        })
        val listener = object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.mainDataList.value?.let { it -> mainRecyclerViewAdapter.updateList(it.filter { newText.toString() in it.name }) }
                return true
            }
        }
        mainSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = viewModel.getFilteredList(newText)
                mainRecyclerViewAdapter.updateList(filteredList)
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