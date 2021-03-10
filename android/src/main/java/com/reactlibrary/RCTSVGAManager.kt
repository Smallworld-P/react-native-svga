package com.reactlibrary

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import java.net.URL

class RCTSVGAManager : SimpleViewManager<RCTSVGAImageView>() {
    override fun getName(): String {
        return REACT_CLASS
    }

    public override fun createViewInstance(c: ThemedReactContext): RCTSVGAImageView {
        return RCTSVGAImageView(c, null, 0)
    }

    @ReactProp(name = "source")
    fun setSource(view: RCTSVGAImageView, source: String) {
        val svgaParser = SVGAParser.shareParser()
        if (source.startsWith("http")) {
            svgaParser.decodeFromURL(URL(source), object : SVGAParser.ParseCompletion {
                override fun onComplete(videoItem: SVGAVideoEntity) {
                    view.setVideoItem(videoItem)
                    view.startAnimation()
                }
                override fun onError() {}
            })
        } else {
            svgaParser.decodeFromAssets(source, object : SVGAParser.ParseCompletion {
                override fun onComplete(videoItem: SVGAVideoEntity) {
                    view.setVideoItem(videoItem)
                    view.startAnimation()
                }
                override fun onError() {}
            })
        }
    }

    @ReactProp(name = "loops", defaultInt = 0)
    fun setLoops(view: RCTSVGAImageView, loops: Int) {
        view.loops = loops
    }

    @ReactProp(name = "clearsAfterStop", defaultBoolean = true)
    fun setClearsAfterStop(view: RCTSVGAImageView, clearsAfterStop: Boolean?) {
        view.clearsAfterStop = clearsAfterStop!!
    }

    @ReactProp(name = "currentState")
    fun setCurrentState(view: RCTSVGAImageView, currentState: String?) {
        view.currentState = currentState
        when (currentState) {
            "start" -> view.startAnimation()
            "pause" -> view.pauseAnimation()
            "stop" -> view.stopAnimation()
            "clear" -> view.stopAnimation(true)
            else -> {
            }
        }
    }

    @ReactProp(name = "toFrame", defaultInt = -1)
    fun setToFrame(view: RCTSVGAImageView, toFrame: Int) {
        if (toFrame < 0) {
            return
        }
        view.stepToFrame(toFrame, view.currentState == "play")
    }

    @ReactProp(name = "toPercentage", defaultFloat = -1.0f)
    fun setToPercentage(view: RCTSVGAImageView, toPercentage: Float) {
        if (toPercentage < 0) {
            return
        }
        view.stepToPercentage(toPercentage.toDouble(), view.currentState == "play")
    }

    companion object {
        const val REACT_CLASS = "RNSVGA"
    }
}