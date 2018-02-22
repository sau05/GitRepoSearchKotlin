package com.saurabh.gitreposearchkotlin.listener

import android.view.View

/**
 * Created by kiris on 2/21/2018.
 */
interface RecycleViewClickListener {
    abstract fun onItemClick(v: View, position: Int)

}