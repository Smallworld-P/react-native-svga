import React from 'react';
import { ViewProps } from 'react-native';
interface IP extends ViewProps {
    onFinished?: () => void;
    onFrame?: (value: number) => void;
    onPercentage?: (value: number) => void;
    source: string;
}
interface IS {
    source: string;
    toFrame: number;
    currentState: string;
    toPercentage: number;
}
export class SVGAView extends React.Component<IP, IS> {
    constructor(props: Readonly<IP>);
    load(source: string): void;
    startAnimation(): void;
    pauseAnimation(): void;
    stopAnimation(): void;
    stepToFrame(toFrame: any, andPlay: boolean): void;
    stepToPercentage(toPercentage: any, andPlay: boolean): void;
    componentWillUnmount(): void;
    render(): JSX.Element | null;
}

export class SVGAModule {
    // static isCached(url: String, callback: any): void
    static isCached(url: String): Promise<Boolean>
    static advanceDownload(urls: Array<String>): void
}
