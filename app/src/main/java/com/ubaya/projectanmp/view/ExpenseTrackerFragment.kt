package com.ubaya.projectanmp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.projectanmp.databinding.FragmentExpenseTrackerBinding
import com.ubaya.projectanmp.util.SessionManager
import com.ubaya.projectanmp.viewmodel.BudgetViewModel
import com.ubaya.projectanmp.viewmodel.ExpenseViewModel

class ExpenseTrackerFragment : Fragment() {

    private lateinit var binding: FragmentExpenseTrackerBinding
    private lateinit var vm: ExpenseViewModel
    private lateinit var budgetVm: BudgetViewModel
    private lateinit var adapter: ExpenseListAdapter
    private var userId = -1
    private val budgetMap = mutableMapOf<Int, String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentExpenseTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(requireActivity())[ExpenseViewModel::class.java]
        budgetVm = ViewModelProvider(requireActivity())[BudgetViewModel::class.java]
        userId = SessionManager.userId(requireContext())

        val provider: (Int) -> String = { id ->
            budgetMap[id] ?: "-"
        }

        adapter = ExpenseListAdapter(arrayListOf(), provider)
        binding.recExpense.layoutManager = LinearLayoutManager(context)
        binding.recExpense.adapter = adapter

        binding.refreshLayout.setOnRefreshListener {
            vm.refresh(userId)
            budgetVm.refresh(userId)
            binding.refreshLayout.isRefreshing = false
        }

        binding.fabAdd.setOnClickListener {
            val action = ExpenseTrackerFragmentDirections
                .actionExpenseTrackerFragmentToNewExpenseFragment()
            Navigation.findNavController(it).navigate(action)
        }

        observe()
        vm.refresh(userId)
        budgetVm.refresh(userId)
    }

    private fun observe() {
        budgetVm.budgetsLD.observe(viewLifecycleOwner) { budgets ->
            budgetMap.clear()
            budgets.forEach {
                budgetMap[it.budget.id] = it.budget.name
            }
            vm.expensesLD.value?.let { adapter.update(it) }
        }

        vm.expensesLD.observe(viewLifecycleOwner) { list ->
            adapter.update(list)
            binding.progressLoad.visibility = View.GONE
        }
    }
}
