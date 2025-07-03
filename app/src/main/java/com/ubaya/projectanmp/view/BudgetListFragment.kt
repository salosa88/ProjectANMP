package com.ubaya.projectanmp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.projectanmp.databinding.FragmentBudgetListBinding
import com.ubaya.projectanmp.viewmodel.BudgetViewModel

class BudgetListFragment : Fragment() {

    private lateinit var binding: FragmentBudgetListBinding
    private lateinit var vm: BudgetViewModel
    private val adapter = BudgetListAdapter(arrayListOf()) { id ->
        // Edit
        val action = BudgetListFragmentDirections
            .actionBudgetListFragmentToNewBudgetFragment(id)
        view?.let { Navigation.findNavController(it).navigate(action) }
    }

    private var userId = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentBudgetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(requireActivity()).get(BudgetViewModel::class.java)
        userId = com.ubaya.projectanmp.util.SessionManager.userId(requireContext())

        binding.recBudget.layoutManager = LinearLayoutManager(context)
        binding.recBudget.adapter = adapter

        binding.refreshLayout.setOnRefreshListener {
            vm.refresh(userId)
            binding.refreshLayout.isRefreshing = false
        }

        binding.fabAdd.setOnClickListener {
            val action = BudgetListFragmentDirections
                .actionBudgetListFragmentToNewBudgetFragment(-1) // -1 = new
            Navigation.findNavController(it).navigate(action)
        }

        observeViewModel()
        vm.refresh(userId)           // inisiasi load
    }

    private fun observeViewModel() {
        vm.budgetsLD.observe(viewLifecycleOwner, Observer {
            adapter.updateList(it)
            binding.progressLoad.visibility = View.GONE
        })
    }
}

