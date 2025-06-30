package com.ubaya.projectanmp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.projectanmp.databinding.ItemBudgetBinding
import com.ubaya.projectanmp.model.BudgetWithExpenses
import java.text.NumberFormat
import java.util.Locale

class BudgetListAdapter(
    private val budgets: ArrayList<BudgetWithExpenses>,
    private val listener: (Int) -> Unit  // klik -> id budget
) : RecyclerView.Adapter<BudgetListAdapter.BudgetViewHolder>() {

    class BudgetViewHolder(val binding: ItemBudgetBinding)
        : RecyclerView.ViewHolder(binding.root)

    private val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BudgetViewHolder(
            ItemBudgetBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = budgets.size

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val item = budgets[position]

        holder.binding.txtName.text = item.budget.name
        holder.binding.txtInfo.text = "${rupiah.format(item.spent)} / ${rupiah.format(item.budget.maxAmount)}"

        val percent = (item.spent * 100 / item.budget.maxAmount).coerceAtMost(100)
        holder.binding.progress.progress = percent
        holder.itemView.setOnClickListener { listener(item.budget.id) }
    }

    fun updateList(newList: List<BudgetWithExpenses>) {
        budgets.clear()
        budgets.addAll(newList)
        notifyDataSetChanged()
    }
}