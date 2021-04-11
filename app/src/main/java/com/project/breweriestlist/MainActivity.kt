package com.project.breweriestlist

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.breweriestlist.adapter.BreweriesAdapter
import com.project.breweriestlist.data.Brewery
import com.project.breweriestlist.viewmodel.BreweriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

private const val LOG_TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: BreweriesViewModel
    private lateinit var adapter: BreweriesAdapter
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(LOG_TAG, "onCreate(): ")

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.action_bar)

        handler = Handler()

        adapter = BreweriesAdapter(mutableListOf())
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter

        viewModel.breweryList.observe(this, Observer { newList ->
            refreshAdapterData(newList)
        })

        viewModel.searchList.observe(this, Observer { newList ->
            refreshAdapterData(newList)
        })

        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.makeSearch(s.toString(), applicationContext)
            }
        })
    }

    private fun refreshAdapterData(newList: List<Brewery>) = adapter.setData(newList)
}
