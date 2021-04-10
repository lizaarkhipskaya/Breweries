package com.project.breweriestlist.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.breweriestlist.R
import com.project.breweriestlist.data.Brewery
import kotlinx.android.synthetic.main.item_brewery.view.*
import java.util.*

private const val LOG_TAG = "BreweriesAdapter"

class BreweriesAdapter(private var dataList: MutableList<Brewery>) :
    RecyclerView.Adapter<BreweriesAdapter.BreweryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreweryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_brewery, parent, false)
        return BreweryViewHolder(
            itemView
        )
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: BreweryViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    fun setData(newList: List<Brewery>) {
        dataList.clear()
        dataList.addAll(newList)
        Log.i(LOG_TAG, "setData(): size ${newList.size}")
        notifyDataSetChanged()
    }


    class BreweryViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {

        private val spanColor = view.resources.getColor(R.color.colorTextInfoValue)

        fun bind(brewery: Brewery) {
            view.apply {
                name.text = brewery.name

                country.updateTextWithSpan(
                    resources.getString(R.string.brewery_item_country),
                    getSpanText(brewery.country)
                )
                state.updateTextWithSpan(
                    resources.getString(R.string.brewery_item_state),
                    getSpanText(brewery.state)
                )
                city.updateTextWithSpan(
                    resources.getString(R.string.brewery_item_city),
                    getSpanText(brewery.city)
                )
                phone.updateTextWithSpan(
                    resources.getString(R.string.brewery_item_phone),
                    getSpanText(brewery.phone.toString())
                )
                website.updateTextWithSpan(
                    resources.getString(R.string.brewery_item_website),
                    getSpanText(
                        brewery.websiteUrl?.toLowerCase(Locale.ENGLISH).toString()
                    )
                )

                setupMapButton(map_button, brewery)
            }
        }

        private fun setupMapButton(mapButton: Button, brewery: Brewery) {
            if (brewery.areCoordinatesValid()) {
                mapButton.visibility = View.VISIBLE
                mapButton.setOnClickListener(
                    MapButtonClickListener(
                        brewery.latitude,
                        brewery.longitude
                    )
                )
            } else {
                mapButton.visibility = View.GONE
            }
        }

        private fun TextView.updateTextWithSpan(text: String?, spanText: SpannableString) {
            this.text = text
            this.append(spanText)
            updateVisibilityByTextLength(spanText)
        }

        private fun TextView.updateVisibilityByTextLength(text: SpannableString) {
            this.visibility = if (text.isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        private fun getSpanText(text: String) =
            SpannableString(text).apply {
                setSpan(
                    ForegroundColorSpan(spanColor),
                    0,
                    text.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
    }
}