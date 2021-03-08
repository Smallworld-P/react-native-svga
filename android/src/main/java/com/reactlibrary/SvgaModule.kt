package com.reactlibrary

import android.net.http.HttpResponseCache
import android.util.Log
import com.facebook.react.bridge.*
import com.opensource.svgaplayer.SVGACache
import com.opensource.svgaplayer.SVGACache.isCached
import com.opensource.svgaplayer.SVGACache.onCreate
import com.opensource.svgaplayer.SVGAParser.Companion.shareParser
import java.io.File
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList

class SvgaModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private val reactContext: ReactApplicationContext
    override fun getName(): String {
        return "RCTSvgaMoudle"
    }

    @ReactMethod
    fun sampleMethod(stringArgument: String, numberArgument: Int, callback: Callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("Received numberArgument: $numberArgument stringArgument: $stringArgument")
    }

    @ReactMethod
    fun isCached(url: String, promise: Promise) {
        val res = isCached(url)
        Log.d("TAG", "isCached = $res $url")
        promise.resolve(res)
    }

    @ReactMethod
    fun advanceDownload(urls: ReadableArray?) {
        Log.d("TAG", "advanceDownload1: " + urls?.size())
        if (urls != null && urls.size() > 0) {
            val svgaParser = shareParser()
            svgaParser.decodeFromURL(URL(urls.getString(0)), null)
//            val list = urls.toArrayList()
//            for (i in list) {
//                println("i = ${i.toString()}")
//                svgaParser.decodeFromURL(URL(i.toString()), null)
//            }
        }
    }

    init {
        // 设置缓存
        val cacheDir = File(reactContext.cacheDir, "http")
        HttpResponseCache.install(cacheDir, 1024 * 1024 * 128)
        onCreate(reactContext, SVGACache.Type.FILE)
        this.reactContext = reactContext
    }
}