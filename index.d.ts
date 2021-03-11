import React from 'react'
import { ViewProps } from 'react-native'
export interface ISvgaProps extends ViewProps {
    /// 动画播放完成后，回调
    onFinished?: () => void
    /// 动画播放至某帧时，回调
    onFrame?: (value: number) => void
    /// 动画播放至某进度时，回调
    onPercentage?: (value: number) => void
    /// SVGA 动画文件的路径，可以是 URL，或是本地 NSBundle.mainBundle / assets 文件
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
    currentState?: "start" | "pause" | "stop" | "clear"
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
    /// 加载动画
    load(source: string): void
    /// 开始执行动画
    startAnimation(): void
    /// 暂停动画
    pauseAnimation(): void
    /// 停止动画
    stopAnimation(): void
    /// 清空动画
    clearAnimation(): void
    /// 渲染特定的帧，如果andPlay设置为true，则从该帧开始播放
    stepToFrame(toFrame: any, andPlay: boolean): void
    /// 渲染特定百分比的帧，如果将percentage值设置为andPlaytrue，则该值应从0.0to到1.0该帧播放
    stepToPercentage(toPercentage: any, andPlay: boolean): void
    componentWillUnmount(): void
    render(): JSX.Element | null
}

export class SVGAModule {
    // static isCached(url: String, callback: any): void
    static isCached(url: String): Promise<Boolean>
    static advanceDownload(urls: Array<String>): void
}
