package com.reactlibrary;

import android.net.http.HttpResponseCache;
import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableArray;
import com.opensource.svgaplayer.SVGACache;
import com.opensource.svgaplayer.SVGAParser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SvgaModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public SvgaModule(ReactApplicationContext reactContext) {
        super(reactContext);
        // 设置缓存
//        val cacheDir = File(context.applicationContext.cacheDir, "http")
//        HttpResponseCache.install(cacheDir, 1024 * 1024 * 128)
//        SVGACache.onCreate(this, SVGACache.Type.FILE)
        try {
            File file = new File(reactContext.getCacheDir(), "http");
            HttpResponseCache.install(file, 1024 * 1024 * 128);
            SVGACache.INSTANCE.onCreate(reactContext, SVGACache.Type.FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RCTSvgaMoudle";
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }

    @ReactMethod
    public void isCached(String url, Promise promise) {
        Boolean res = SVGACache.INSTANCE.isCached(url);
        Log.d("TAG", "isCached = " + res + " " + url);
        promise.resolve(res);
    }

    @ReactMethod
    public void advanceDownload(ReadableArray urls) {
        Log.d("TAG", "advanceDownload1: " + urls.size());
        if (urls != null && urls.size() > 0) {
            SVGAParser svgaParser = SVGAParser.Companion.shareParser();
            for (int i=0; i < urls.size(); i++) {
                try {
                    svgaParser.decodeFromURL(new URL(urls.getString(i)), null);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
