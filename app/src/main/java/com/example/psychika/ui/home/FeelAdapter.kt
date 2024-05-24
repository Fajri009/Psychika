package com.example.psychika.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.psychika.R
import com.example.psychika.data.Feel
import com.example.psychika.databinding.ItemRowFeelBinding

class FeelAdapter(private val listFeel: ArrayList<Feel>) : RecyclerView.Adapter<FeelAdapter.ViewHolder>() {
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFeel: ImageView = itemView.findViewById(R.id.iv_feel)
        val tvDesc: TextView = itemView.findViewById(R.id.tv_desc_feel)
        val boxLayout: LinearLayout = itemView.findViewById(R.id.box_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_feel, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listFeel.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (photo, desc) = listFeel[position]
        holder.ivFeel.setImageResource(photo)
        holder.tvDesc.text = desc

        holder.itemView.setOnClickListener {
            val previousSelectedItemPosition = selectedItemPosition
            selectedItemPosition = holder.adapterPosition

            if (previousSelectedItemPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(previousSelectedItemPosition)
            }

            notifyItemChanged(selectedItemPosition)
        }

        if (position == selectedItemPosition) {
            holder.boxLayout.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.primary_200))
        } else {
            holder.boxLayout.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.primary_50))
        }
    }
}
