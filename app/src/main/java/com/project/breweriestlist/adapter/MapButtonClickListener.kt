package com.project.breweriestlist.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import com.project.breweriestlist.R

private const val MAPS_PACKAGE = "com.google.android.apps.maps"
private const val LOG_TAG = "MapButtonClickListener"

class MapButtonClickListener(
    private val latitude: Double,
    private val longitude: Double
) : View.OnClickListener {

    override fun onClick(v: View?) {
        val gmmIntentUri =
            Uri.parse("google.streetview:cbll=${latitude},${longitude}}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage(MAPS_PACKAGE)
        try {
            v?.context?.startActivity(mapIntent)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "failed to open maps: ${e.message}")
            v?.context?.apply {
                Toast.makeText(this, R.string.toast_maps_failed, Toast.LENGTH_LONG).show()
            }
        }
    }
}