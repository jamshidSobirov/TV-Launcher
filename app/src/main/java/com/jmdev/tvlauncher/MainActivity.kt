package com.jmdev.tvlauncher

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : FragmentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var appAdapter: AppAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        updateTime()
        startClockUpdater()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.app_recycler_view)

        val apps = getInstalledApps()

        appAdapter = AppAdapter(apps)
        recyclerView.adapter = appAdapter

        recyclerView.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                val viewHolder = recyclerView.getChildViewHolder(view)
                if (viewHolder.adapterPosition == 0) {
                    view.requestFocus()
                    recyclerView.removeOnChildAttachStateChangeListener(this)
                }
            }

            override fun onChildViewDetachedFromWindow(view: View) {}
        })

        val columnCount = calculateBestColumnCount(140)
        recyclerView.layoutManager = GridLayoutManager(this, columnCount)

        findViewById<ImageView>(R.id.btnQuit).setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.btnSettings).setOnClickListener {
            try {
                val intent = Intent(android.provider.Settings.ACTION_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Unable to open settings", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getInstalledApps(): List<ResolveInfo> {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        return packageManager.queryIntentActivities(intent, 0)
            .filterNot { it.activityInfo.packageName == packageName }
            .sortedBy { it.loadLabel(packageManager).toString().lowercase() }
    }

    private fun calculateBestColumnCount(itemWidthDp: Int): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / itemWidthDp).toInt().coerceAtLeast(2)
    }

    private fun updateTime() {
        val currentTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        findViewById<TextView>(R.id.tvTime).text = formatter.format(currentTime)
    }

    private fun startClockUpdater() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                updateTime()
                handler.postDelayed(this, 60_000)
            }
        }
        handler.post(runnable)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }



}

