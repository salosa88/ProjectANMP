package com.ubaya.projectanmp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.projectanmp.databinding.ItemReportBinding
import com.ubaya.projectanmp.model.BudgetWithExpenses
import java.text.NumberFormat
import java.util.*

class ReportListAdapter(
    private val list: ArrayList<BudgetWithExpenses>
) : RecyclerView.Adapter<ReportListAdapter.ReportVH>() {

    class ReportVH(val binding: ItemReportBinding)
        : RecyclerView.ViewHolder(binding.root)

    private val rupiah = NumberFormat.getCurrencyInstance(Locale("in","ID"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ReportVH(ItemReportBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ReportVH, position: Int) {
        val item = list[position]
        holder.binding.txtName.text = item.budget.name
        holder.binding.txtInfo.text =
            "${rupiah.format(item.spent)} / ${rupiah.format(item.budget.maxAmount)}"

        val percent = (item.spent * 100 / item.budget.maxAmount).coerceAtMost(100)
        holder.binding.progress.progress = percent

        val left = item.budget.maxAmount - item.spent
        holder.binding.txtLeft.text = "Budget left: ${rupiah.format(left)}"
    }

    fun update(data: List<BudgetWithExpenses>) {
        list.clear(); list.addAll(data); notifyDataSetChanged()
    }
}
