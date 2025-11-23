package com.nomasmatchapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nomasmatchapp.databinding.ItemRoundBinding
import com.nomasmatchapp.model.Round

class RoundsAdapter(
    private val matchClickListener: OnMatchClickListener
) : RecyclerView.Adapter<RoundsAdapter.RoundViewHolder>() {

    private var rounds: List<Round> = emptyList()

    class RoundViewHolder(
        private val binding: ItemRoundBinding,
        private val listener: OnMatchClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private val matchesAdapter = MatchesAdapter(listener)

        init {
            binding.recyclerViewMatches.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = matchesAdapter
            }
        }

        fun bind(round: Round) {
            binding.tvRoundTitle.text = "JORNADA " + round.jornada.toString()
            binding.tvRoundDate.text = round.partidos.firstOrNull()?.fecha ?: ""
            // 3. CORRECCIÓN: Pasa la lista de partidos al adapter anidado usando la propiedad correcta 'matches'.
            matchesAdapter.submitList(round.partidos)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoundViewHolder {
        val binding = ItemRoundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoundViewHolder(binding, matchClickListener)
    }

    override fun getItemCount(): Int = rounds.size

    override fun onBindViewHolder(holder: RoundViewHolder, position: Int) {
        val round = rounds[position]
        holder.bind(round)
    }

    fun submitList(newRounds: List<Round>) {
        rounds = newRounds
        notifyDataSetChanged()
    }
}
