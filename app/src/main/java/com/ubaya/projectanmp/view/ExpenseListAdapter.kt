package com.ubaya.projectanmp.view

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.projectanmp.databinding.ItemExpenseBinding
import com.ubaya.projectanmp.model.Expense
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpenseListAdapter(
    private val list: ArrayList<Expense>,
    private val budgetNameProvider: (Int) -> String
) : RecyclerView.Adapter<ExpenseListAdapter.ExpVH>() {

    class ExpVH(val binding: ItemExpenseBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val dateFmt  = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
    private val rupiah   = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ExpVH(ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ExpVH, position: Int) {
        val exp = list[position]
        val dateTime = dateFmt.format(Date(exp.createdAt * 1000))

        holder.binding.txtDateTime.text = dateTime
        holder.binding.chipBudget.text  = budgetNameProvider(exp.budgetId)
        holder.binding.txtAmount.text   = rupiah.format(exp.amount)

        holder.binding.txtAmount.setOnClickListener { v: View ->
            AlertDialog.Builder(v.context)
                .setTitle("Expense Detail")
                .setMessage(
                    """
                    Tanggal   : $dateTime
                    Budget    : ${holder.binding.chipBudget.text}
                    Nominal   : ${rupiah.format(exp.amount)}
                    Keterangan: ${exp.description}
                    """.trimIndent()
                )
                .setPositiveButton("OK", null)
                .show()
        }
    }

    fun update(newList: List<Expense>) {
        list.clear(); list.addAll(newList); notifyDataSetChanged()
    }
}