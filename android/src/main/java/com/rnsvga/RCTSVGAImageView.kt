package com.rnsvga

import android.content.Context
import android.util.AttributeSet
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.opensource.svgaplayer.SVGACallback
import com.opensource.svgaplayer.SVGAImageView

class RCTSVGAImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SVGAImageView(context, attrs, defStyleAttr)  {
    internal var currentState: String? = null

    init {
        callback = object : SVGACallback {

            override fun onPause() { }

            override fun onFinished() {
                (context as? ReactContext)?.let {
                    val map = Arguments.createMap()
                    map.putString("action", "onFinished")
                    it.getJSModule(RCTEventEmitter::class.java).receiveEvent(id, "topChange", map)
                }
            }

            override fun onRepeat() { }

            override fun onStep(frame: Int, percentage: Double) {
                (context as? ReactContext)?.let {
                    val map = Arguments.createMap()
                    map.putString("action", "onFrame")
                    map.putInt("value", frame)
                    it.getJSModule(RCTEventEmitter::class.java).receiveEvent(id, "topChange", map)
                }
                (context as? ReactContext)?.let {
                    val map = Arguments.createMap()
                    map.putString("action", "onPercentage")
                    map.putDouble("value", percentage)
                    it.getJSModule(RCTEventEmitter::class.java).receiveEvent(id, "topChange", map)
                }
            }

        }
    }
}