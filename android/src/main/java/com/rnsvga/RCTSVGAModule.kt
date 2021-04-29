package com.rnsvga

import android.net.http.HttpResponseCache
import com.facebook.react.bridge.*
import com.opensource.svgaplayer.SVGACache
import com.opensource.svgaplayer.SVGACache.isCached
import com.opensource.svgaplayer.SVGACache.onCreate
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAParser.Companion.shareParser
import com.opensource.svgaplayer.SVGAVideoEntity
import com.opensource.svgaplayer.utils.log.SVGALogger
import java.io.File
import java.net.URL

class RCTSVGAModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private val reactContext: ReactApplicationContext = reactContext
    override fun getName(): String {
        return "RCTSvgaMoudle"
    }

    private fun isCachedForKT(url: String): Boolean {
        val cacheKey = SVGACache.buildCacheKey(url)
        return isCached(cacheKey)
    }

    /**
     * 是否已下载
     */
    @ReactMethod
    fun isCached(url: String, promise: Promise) {
        val cacheKey = SVGACache.buildCacheKey(url);
        val res = isCached(cacheKey)
        promise.resolve(res)
    }

    /**
     * 预下载
     */
    @ReactMethod
    fun advanceDownload(urls: ReadableArray?) {
        if (urls != null && urls.size() > 0) {
            onCreate(reactContext, SVGACache.Type.FILE)
            val svgaParser = shareParser()
            val list = urls.toArrayList()

            for (url in list) {
                if (url.toString().startsWith("http")) {
                    svgaParser.decodeFromURL(URL(url.toString()), object : SVGAParser.ParseCompletion {
                        override fun onComplete(videoItem: SVGAVideoEntity) {
                            println("$url 缓存成功")
                        }

                        override fun onError() {
                            println("$url 缓存失败,请检查地址是否正确")
                        }
                    })
                } else {
                    println("$url 不是网络地址,不缓存")
                }
            }
        }
    }

    init {
        val cacheDir = File(reactContext.cacheDir, "http")
        HttpResponseCache.install(cacheDir, 1024 * 1024 * 128)
        onCreate(reactContext, SVGACache.Type.FILE)
        shareParser().init(reactContext)
        SVGALogger.setLogEnabled(true)
    }
}