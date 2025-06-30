package com.ubaya.projectanmp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.Locale
import com.ubaya.projectanmp.databinding.FragmentReportBinding
import com.ubaya.projectanmp.util.SessionManager
import com.ubaya.projectanmp.viewmodel.BudgetViewModel
import java.text.NumberFormat

class ReportFragment : Fragment() {

    private lateinit var binding: FragmentReportBinding
    private lateinit var vm: BudgetViewModel
    private val adapter = ReportListAdapter(arrayListOf())
    private val rupiah = NumberFormat.getCurrencyInstance(Locale("in","ID"))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(requireActivity()).get(BudgetViewModel::class.java)

        binding.recReport.layoutManager = LinearLayoutManager(context)
        binding.recReport.adapter = adapter

        observe()
        val uid = SessionManager.userId(requireContext())
        vm.refresh(uid)
    }

    private fun observe() {
        vm.budgetsLD.observe(viewLifecycleOwner) { list ->
            adapter.update(list)

            val totalSpent = list.sumOf { it.spent }
            val totalMax   = list.sumOf { it.budget.maxAmount }
            binding.txtTotal.text =
                "Total expense: ${rupiah.format(totalSpent)} / ${rupiah.format(totalMax)}"
        }
    }
}