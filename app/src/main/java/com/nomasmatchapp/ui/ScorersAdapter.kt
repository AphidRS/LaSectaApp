package com.nomasmatchapp.ui
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.nomasmatchapp.R
import com.nomasmatchapp.databinding.ItemScorerBinding
import com.nomasmatchapp.model.ScorersModels

class ScorersAdapter(private var scorers: MutableList<ScorersModels>) : RecyclerView.Adapter<ScorersAdapter.ScorerViewHolder>() {

    private val baseImageUrl = "https://appweb.rffm.es"
    inner class ScorerViewHolder(val binding: ItemScorerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScorerViewHolder {
        val binding = ItemScorerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScorerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScorerViewHolder, position: Int) {
        val scorer = scorers[position]

        with(holder.binding) {
            tvPlayerName.text = scorer.playerName
            tvTeamName.text = scorer.teamName
            tvGoals.text = scorer.goals

            val fullShieldUrl = baseImageUrl + scorer.teamShieldUrl
            ivTeamShield.load(fullShieldUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }
        }
    }

    override fun getItemCount(): Int = scorers.size
    fun updateData(newScorers: List<ScorersModels>) {
        this.scorers.clear()
        this.scorers.addAll(newScorers)
        notifyDataSetChanged()
    }
}



