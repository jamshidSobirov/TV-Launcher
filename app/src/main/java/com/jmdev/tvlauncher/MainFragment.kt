package com.jmdev.tvlauncher

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.FocusHighlight
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ItemBridgeAdapter
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.VerticalGridPresenter
import androidx.leanback.widget.VerticalGridView

class MainFragment : Fragment() {
    private lateinit var appsGrid: VerticalGridView
    private lateinit var settingsButton: ImageButton
    private lateinit var packageManager: PackageManager
    private lateinit var appAdapter: ArrayObjectAdapter
    private lateinit var bridgeAdapter: ItemBridgeAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        packageManager = requireActivity().packageManager
        appsGrid = view.findViewById(R.id.apps_grid)
        settingsButton = view.findViewById(R.id.settings_button)

        setupGrid()
        setupSettingsButton()
        loadApps()
    }

    private fun setupGrid() {
        // Configure grid appearance
        appsGrid.setNumColumns(5)
        appsGrid.horizontalSpacing = 10
        appsGrid.verticalSpacing = 10

        // Create adapters
        appAdapter = ArrayObjectAdapter(AppCardPresenter())
        bridgeAdapter = ItemBridgeAdapter(appAdapter)

        // Set the adapter
        appsGrid.adapter = bridgeAdapter
    }

    private fun setupSettingsButton() {
        settingsButton.setOnClickListener {
            openSettings()
        }

        // Make sure button is focusable for TV remote
        settingsButton.isFocusable = true
        settingsButton.isFocusableInTouchMode = true
    }

    private fun loadApps() {
        val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val apps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(mainIntent, PackageManager.ResolveInfoFlags.of(0))
        } else {
            packageManager.queryIntentActivities(mainIntent, 0)
        }

        appAdapter.addAll(0, apps.sortedBy { it.loadLabel(packageManager).toString() })
    }

    private fun launchApp(appInfo: ResolveInfo) {
        val intent = packageManager.getLaunchIntentForPackage(appInfo.activityInfo.packageName)
        startActivity(intent)
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


}