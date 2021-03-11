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
            var index = 0
            var pause = false
            val lock = java.lang.Object()
            val svgaParser = shareParser()
            val list = urls.toArrayList()

            Thread(Runnable {
                for (i in list) {
                    index++
                    if (!isCachedForKT(i.toString())) {
                        while (pause) {
                            synchronized(lock) {
                                lock.wait()
                            }
                        }
                        println("缓存中 - 当前第 $index 个, 共 ${urls.size()} 个, 地址 : $i")
                        pause = true
                        svgaParser.decodeFromURL(URL(i.toString()), object : SVGAParser.ParseCompletion {
                            override fun onComplete(videoItem: SVGAVideoEntity) {
                                println("第 $index 个 $i 缓存成功")
                                pause = false
                                synchronized (lock) {
                                    lock.notifyAll();
                                }
                            }

                            override fun onError() {
                                println("第 $index 个 $i 缓存失败,请检查地址是否正确")
                                pause = false
                                synchronized (lock) {
                                    lock.notifyAll();
                                }
                            }
                        })
                    } else {
                        println("第 $index 个, 地址 : $i 已缓存")
                    }
                }
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