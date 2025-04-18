package com.jmdev.tvlauncher

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AppAdapter(private val apps: List<ResolveInfo>) : RecyclerView.Adapter<AppViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = apps[position]
        val icon = app.loadIcon(holder.itemView.context.packageManager)
        val label = app.loadLabel(holder.itemView.context.packageManager)
        holder.bind(icon, label.toString()) {
            launchApp(app, holder.itemView.context)
        }
    }

    override fun getItemCount() = apps.size

    private fun launchApp(app: ResolveInfo, context: Context) {
        val intent = Intent()
        intent.setClassName(app.activityInfo.packageName, app.activityInfo.name)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
