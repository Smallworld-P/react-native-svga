'use strict'

import React, { Component } from 'react'
import { 
    requireNativeComponent, 
    Platform 
} from 'react-native';

const NativeSVGAView = requireNativeComponent('RNSVGA', SvgaView)

export default class SvgaView extends Component {

    constructor(props) {
        super(props)
        this.state = {}
    }

    /** 
     * 加载动画
     * source 数据源
     */
    load(source) {
        this.setState({
            source
        });
    }

    /** 开始动画 */
    startAnimation() {
        this.setState({
            currentState: "start",
        });
    }

    /** 暂停动画 */
    pauseAnimation() {
        this.setState({
            currentState: "pause",
        });
    }

    /** 停止动画 */
    stopAnimation() {
        this.setState({
            currentState: "stop",
        });
    }

    /** 
     * 跳到某帧，然后继续（暂停）播放动画
     * toFrame 指定跳到某一帧
     * andPlay 播放状态
     */
    stepToFrame(toFrame, andPlay) {
        this.setState({
            currentState: andPlay === true ? "play" : "pause",
            toFrame: -1,
        }, () => {
            this.setState({
                toFrame,
            });
        });
    }

    /** 
     * 跳到某进度，然后继续（暂停）播放动画
     * toFrame 指定跳到某进度
     * andPlay 播放状态
     */
    stepToPercentage(toPercentage, andPlay) {
        this.setState({
            currentState: andPlay === true ? "play" : "pause",
            toPercentage: -1,
        }, () => {
            this.setState({
                toPercentage,
            });
        });
    }

    componentWillUnmount() {
        this.stopAnimation();
    }

    render() {
        if (!this.props.source) {
            return null;
        }

        let eventListeners = {};
        if (Platform.OS === "android") {
            eventListeners.onChange = (event) => {
                const { action } = event.nativeEvent;
                if (action === "onFinished") {
                    if (typeof this.props.onFinished === "function") {
                        this.props.onFinished();
                    }
                }
                else if (action === "onFrame") {
                    if (typeof this.props.onFrame === "function") {
                        this.props.onFrame(event.nativeEvent.value);
                    }
                }
                else if (action === "onPercentage") {
                    if (typeof this.props.onPercentage === "function") {
                        this.props.onPercentage(event.nativeEvent.value);
                    }
                }
            }
        }
        else if (Platform.OS === "ios") {
            if (typeof this.props.onFrame === "function") {
                eventListeners.onFrame = (event) => {
                    console.log(event);
                    this.props.onFrame(event.nativeEvent.value);
                }
            }
            if (typeof this.props.onPercentage === "function") {
                eventListeners.onPercentage = (event) => {
                    console.log(event);
                    this.props.onPercentage(event.nativeEvent.value);
                }
            }
        }

        return (
            <NativeSVGAView {...this.props} {...this.state} {...eventListeners}/>
        )
    }
}
