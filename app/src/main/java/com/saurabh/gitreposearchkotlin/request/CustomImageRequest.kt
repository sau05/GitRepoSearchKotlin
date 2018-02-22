package com.saurabh.gitreposearchkotlin.request

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.ImageLoader

/**
 * Created by kiris on 2/21/2018.
 */
class CustomImageRequest(context:Context) {

    private var customImageRequest:CustomImageRequest?=null
    private var requestQueue: RequestQueue?=null
    private var imageLoader:ImageLoader?=null
    private var mContext=context

    init {
        this.requestQueue=getRequestQueue()
        imageLoader = ImageLoader(requestQueue,
                object : ImageLoader.ImageCache {
                    private val cache = LruCache<String, Bitmap>(20)

                    override fun getBitmap(url: String): Bitmap {
                        return cache.get(url)
                    }

                    override fun putBitmap(url: String, bitmap: Bitmap) {
                        cache.put(url, bitmap)
                    }
                })
    }
    @Synchronized
    fun getInstance(context: Context): CustomImageRequest {
        if (customImageRequest == null) {
            customImageRequest = CustomImageRequest(context)
        }
        return customImageRequest as CustomImageRequest
    }

    fun getRequestQueue():RequestQueue?{
        val cache = DiskBasedCache(mContext.getCacheDir(), 10 * 1024 * 1024)
        val network = BasicNetwork(HurlStack())
        requestQueue = RequestQueue(cache, network)
        requestQueue!!.start()
        return requestQueue
    }

    fun getImageLoader():ImageLoader?{
        return imageLoader
    }
}