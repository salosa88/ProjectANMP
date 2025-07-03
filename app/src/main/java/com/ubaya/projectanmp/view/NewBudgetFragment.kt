package com.ubaya.projectanmp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ubaya.projectanmp.databinding.FragmentNewBudgetBinding
import com.ubaya.projectanmp.model.Budget
import com.ubaya.projectanmp.util.SessionManager
import com.ubaya.projectanmp.viewmodel.BudgetViewModel

class NewBudgetFragment : Fragment() {

    private lateinit var binding: FragmentNewBudgetBinding
    private lateinit var vm: BudgetViewModel
    private var budgetId: Int = -1           // -1 = tambah baru
    private var ownerId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        budgetId = arguments?.getInt("budgetId") ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(requireActivity()).get(BudgetViewModel::class.java)

        ownerId = SessionManager.userId(requireContext())

        if (budgetId != -1) {
            vm.budgetsLD.value
                ?.firstOrNull { it.budget.id == budgetId }
                ?.let { item ->
                    binding.edtName.setText(item.budget.name)
                    binding.edtAmount.setText(item.budget.maxAmount.toString())
                }
        }

        binding.btnSave.setOnClickListener {
            val name   = binding.edtName.text.toString()
            val amount = binding.edtAmount.text.toString().toIntOrNull() ?: -1

            if (budgetId == -1) {                             // Add
                vm.addBudget(ownerId, name, amount)
            } else {                                          // Edit
                vm.updateBudget(
                    Budget(budgetId, ownerId, name, amount),
                    amount, ownerId
                )
            }
            findNavController().popBackStack()
        }

        vm.messageLD.observe(viewLifecycleOwner, Observer {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        })
    }
}