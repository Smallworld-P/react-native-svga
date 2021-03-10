import React from 'react'
import { ViewProps } from 'react-native'
export interface ISvgaProps extends ViewProps {
    /// 动画播放完成后，回调
    onFinished?: () => void
    /// 动画播放至某帧时，回调
    onFrame?: (value: number) => void
    /// 动画播放至某进度时，回调
    onPercentage?: (value: number) => void
    source: string
    /// 默认值为 0，用于指定动画循环次数，0 = 无限循环
    loops?: number
    /// 默认值为 true，动画播放完成后，是否清空画布
    clearsAfterStop?: boolean
    /// 用于控制 SVGA 播放状态，可设定以下值
    /// ‘start’ = 从头开始播放
    /// ‘pause’ = 从当前位置暂停播放
    /// ‘stop’ = ‘停止播放’
    /// ‘clear’ = ‘停止播放并清空画布
    currentState?: string
    /// 控制当前动画停靠在某帧，如果 currentState 值为 ‘play’，则跳到该帧后继续播放动画
    toFrame?: number
    /// 控制当前动画停靠在某进度，如果 currentState 值为 ‘play’，则跳到该帧后继续播放动画
    toPercentage?: number
}
interface IS {
    source: string
    toFrame: number
    currentState: string
    toPercentage: number
}
export class SVGAView extends React.Component<ISvgaProps, IS> {
    constructor(props: Readonly<ISvgaProps>)
    load(source: string): void
    startAnimation(): void
    pauseAnimation(): void
    stopAnimation(): void
    stepToFrame(toFrame: any, andPlay: boolean): void
    stepToPercentage(toPercentage: any, andPlay: boolean): void
    componentWillUnmount(): void
    render(): JSX.Element | null
}

export class SVGAModule {
    // static isCached(url: String, callback: any): void
    static isCached(url: String): Promise<Boolean>
    static advanceDownload(urls: Array<String>): void
}
