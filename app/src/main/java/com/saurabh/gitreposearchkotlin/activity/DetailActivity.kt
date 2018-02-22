package com.saurabh.gitreposearchkotlin.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.TextView
import com.saurabh.gitreposearchkotlin.R
import com.saurabh.gitreposearchkotlin.adapter.SubscriberAdapter
import com.saurabh.gitreposearchkotlin.model.SubscriberResponse

/**
 * Created by kiris on 2/21/2018.
 */
class DetailActivity:AppCompatActivity() {

    internal var subscriberResponseList: List<SubscriberResponse>? = null
    private var rvSubscribers: RecyclerView? = null
    private var tvSubscriberCount: TextView? = null
    private var subscribers_url: String? = null
    private var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_detail)
    }

    fun notifyAdapter(){
        tvSubscriberCount!!.setText(getString(R.string.text_sub_count,subscriberResponseList!!.size.toString()))
        var subScriberAdapter:SubscriberAdapter
        val subscriberAdapter = SubscriberAdapter(applicationContext, subscriberResponseList)


    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}