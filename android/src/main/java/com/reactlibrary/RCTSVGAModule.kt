package com.reactlibrary

import android.net.http.HttpResponseCache
import android.util.Log
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
            val svgaParser = shareParser()
            val list = urls.toArrayList()

            fun downloadUrl(index: Int) {
                if (index < list.size) {
                    println("正在缓存 $index ${list.lastIndex} ${list[index]}")
                    svgaParser.decodeFromURL(URL(list[index].toString()), object : SVGAParser.ParseCompletion {
                            override fun onComplete(videoItem: SVGAVideoEntity) {
                                println("第 $index 个 ${list[index]} 缓存成功")
                                downloadUrl(index + 1)
                            }

                            override fun onError() {
                                println("第 $index 个 ${list[index]} 缓存失败,请检查地址是否正确")
                                downloadUrl(index + 1)
                            }
                        })
                }
            }

            Thread(Runnable {
                downloadUrl(0)
            }).start()
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