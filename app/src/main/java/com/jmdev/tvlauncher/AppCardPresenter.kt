package com.jmdev.tvlauncher

import android.content.pm.ResolveInfo
import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter

class AppCardPresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        val cardView = ImageCardView(parent.context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
            setMainImageDimensions(200, 200)

            // Add click listener to each card
            setOnClickListener {
                // The tag will be set in onBindViewHolder
                val appInfo = it.tag as? ResolveInfo
                appInfo?.let { info ->
                    val intent = context.packageManager.getLaunchIntentForPackage(info.activityInfo.packageName)
                    context.startActivity(intent)
                }
            }
        }

        return Presenter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val appInfo = item as ResolveInfo
        val cardView = viewHolder.view as ImageCardView

        // Store the app info in the view's tag
        cardView.tag = appInfo

        cardView.titleText = appInfo.loadLabel(cardView.context.packageManager).toString()
        cardView.setMainImage(appInfo.loadIcon(cardView.context.packageManager))
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        (viewHolder.view as ImageCardView).mainImage = null
    }
}