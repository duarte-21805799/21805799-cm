package pt.ulusofona.deisi.cm2122.g21805799.ui.adapter

import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import pt.ulusofona.deisi.cm2122.g21805799.R
import pt.ulusofona.deisi.cm2122.g21805799.databinding.ItemFireBinding
import pt.ulusofona.deisi.cm2122.g21805799.ui.viewModels.FireUI
import java.lang.Integer.parseInt
import java.util.*

class FiresListAdapter(
    private var items: List<FireUI> = listOf(),
    private val onClick: (FireUI) -> Unit,
) : RecyclerView.Adapter<FiresListAdapter.FiresListViewHolder>() {
    private var listFull = mutableListOf<FireUI>()

    class FiresListViewHolder(val binding: ItemFireBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiresListViewHolder {
        listFull = items as MutableList<FireUI>
        return FiresListViewHolder(ItemFireBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FiresListViewHolder, position: Int) {
        holder.binding.district.text = items[position].district
        holder.binding.concelho.text = items[position].concelho
        holder.binding.freguesia.text = items[position].freguesia
        holder.binding.state.text = items[position].status
        holder.binding.date.text = items[position].date
        holder.binding.hourItem.text = items[position].hour
        holder.binding.location?.text = items[position].location
        holder.binding.created?.text = items[position].created.toString()
        holder.binding.updated?.text = items[position].updated.toString()
        holder.binding.distance?.text = items[position].distanceFromMe
        holder.binding.away?.visibility = View.VISIBLE
        holder.itemView.setOnClickListener { onClick(items[position]) }
    }

    override fun getItemCount() = items.size

    fun updateItems(items: List<FireUI>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun getSearchFilter(): Filter {
        return searchFilter
    }

    fun getSortAscendingFilter(): Filter {
        return sortAscendingFilter
    }

    fun getSortDescendingFilter(): Filter {
        return sortDescendingFilter
    }

    private val sortAscendingFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<FireUI> = ArrayList()
            filteredList.addAll(listFull)

            filteredList.sortBy {
                parseInt(it.distanceFromMe)
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            updateItems(results.values as List<FireUI>)
            notifyDataSetChanged()
        }
    }

    private val sortDescendingFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<FireUI> = ArrayList()
            filteredList.addAll(listFull)

            filteredList.sortByDescending {
                parseInt(it.distanceFromMe)
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            updateItems(results.values as List<FireUI>)
            notifyDataSetChanged()
        }
    }

    private val searchFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<FireUI> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(listFull)
            } else {
                val filterPattern =
                    constraint.toString().lowercase().trim()
                for (item in listFull) {
                    if (item.district.lowercase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            updateItems(results.values as List<FireUI>)
            notifyDataSetChanged()
        }
    }


}

