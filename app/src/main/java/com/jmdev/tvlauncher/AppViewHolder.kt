package com.jmdev.tvlauncher

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val iconView: ImageView = itemView.findViewById(R.id.app_icon)
    private val nameView: TextView = itemView.findViewById(R.id.app_name)

    fun bind(icon: Drawable, name: String, onClick: () -> Unit) {
        iconView.setImageDrawable(icon)
        nameView.text = name

        itemView.setOnClickListener {
            onClick()
        }

        itemView.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(150).start()
                v.elevation = 10f
                nameView.visibility = View.VISIBLE
            } else {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
                v.elevation = 0f
                nameView.visibility = View.GONE
            }
        }
    }
}
