package com.dicoding.dicodingevents.ui.inactive

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dicodingevents.R
import com.dicoding.dicodingevents.adapter.EventListAdapter
import com.dicoding.dicodingevents.data.response.ListEventsItem
import com.dicoding.dicodingevents.databinding.FragmentInactiveBinding
import com.dicoding.dicodingevents.model.InactiveViewModel
import com.dicoding.dicodingevents.ui.DetailActivity

class InactiveFragment : Fragment() {

    private var _binding: FragmentInactiveBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel : InactiveViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInactiveBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvent.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvEvent.addItemDecoration(itemDecoration)

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.listEventInactive.observe(viewLifecycleOwner) {
            showListEvent(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    viewModel.getInactiveList(query = searchBar.text.toString().trim())
                    searchBar.setText("")
                    false
                }
        }
    }

    private fun showListEvent(eventList : List<ListEventsItem>?) {
        val adapter = EventListAdapter()
        if (eventList!!.isNotEmpty()) {
            adapter.submitList(eventList)
            binding.rvEvent.adapter = adapter
        }

        adapter.setOnItemClickCallback(object : EventListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_EVENT_ID, data.id)
                intent.putExtra(DetailActivity.EXTRA_EVENT_ACTIVE, 0)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.progressBar.progressDrawable = ContextCompat.getDrawable(requireActivity(), R.drawable.custom_progress_bar)
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}