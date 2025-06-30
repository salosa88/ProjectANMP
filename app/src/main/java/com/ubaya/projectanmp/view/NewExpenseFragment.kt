package com.ubaya.projectanmp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ubaya.projectanmp.databinding.FragmentNewExpenseBinding
import com.ubaya.projectanmp.util.SessionManager
import com.ubaya.projectanmp.viewmodel.BudgetViewModel
import com.ubaya.projectanmp.viewmodel.ExpenseViewModel
import java.text.NumberFormat
import java.util.Locale

class NewExpenseFragment : Fragment() {

    private lateinit var binding: FragmentNewExpenseBinding
    private lateinit var expVm: ExpenseViewModel
    private lateinit var budVm: BudgetViewModel

    private var userId           = -1
    private var selectedBudgetId = -1
    private var remaining        = 0
    private val rupiah = NumberFormat.getCurrencyInstance(Locale("in","ID"))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expVm = ViewModelProvider(requireActivity()).get(ExpenseViewModel::class.java)
        budVm = ViewModelProvider(requireActivity()).get(BudgetViewModel::class.java)
        userId = SessionManager.userId(requireContext())
        val sdfNow = java.text.SimpleDateFormat("yyyy-MM-dd hh:mm a", java.util.Locale.getDefault())
        binding.txtNow.text = "Now: ${sdfNow.format(java.util.Date())}"

//        Isi spinner
        val budgets = budVm.budgetsLD.value ?: emptyList()
        val names   = budgets.map { it.budget.name }
        val ids     = budgets.map { it.budget.id }

        binding.spnBudget.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            names
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.spnBudget.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                selectedBudgetId = ids[pos]
                val item = budgets[pos]
                remaining = item.budget.maxAmount - item.spent
                val percent = (item.spent * 100 / item.budget.maxAmount).coerceAtMost(100)
                binding.progressBudget.setProgressCompat(percent, true)
                binding.txtRemaining.text = "Sisa: ${rupiah.format(remaining)}"
            }
        }

//        Add expense
        binding.btnAdd.setOnClickListener {
            val amountStr = binding.edtAmount.text.toString()
            val amount    = amountStr.toIntOrNull()

            when {
                amountStr.isBlank()        -> showSnack("Nominal harus diisi")
                amount == null             -> showSnack("Nominal tidak valid")
                amount < 0                 -> showSnack("Nominal tidak boleh negatif")
                amount > remaining         -> showSnack("Nominal melebihi sisa budget (${rupiah.format(remaining)})")
                selectedBudgetId == -1     -> showSnack("Pilih budget terlebih dahulu")
                else -> {
                    expVm.addExpense(selectedBudgetId, amount,
                        binding.edtNote.text.toString(), userId)
                    findNavController().popBackStack()
                }
            }
        }

        expVm.messageLD.observe(viewLifecycleOwner) { showSnack(it) }
    }


//    Helper snackbar
    private fun showSnack(msg: String) =
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
}
