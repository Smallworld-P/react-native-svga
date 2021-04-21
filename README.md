# react-native-svga

SVGA 是一种同时兼容 iOS / Android / Flutter / Web 多个平台的动画格式。

这是根据原生svga封装的RN插件，说来也简单，无论是安卓的SVGAPlayer-Android还是ios的SVGAPlayer-ios都已经做了React的封装，我们只是稍加改动组合到一起罢了
## 特点
1. 新增预加载
2. 可以把svga资源放js里，而不必放原生项目中

话不多说，进入正题

## 安装
`yarn add react-native-svga`
或者
`npm install react-native-svga --save`

然后
`npx pod-install` 或者 `npx jetify`


### Android 配置
- AndroidManifest.xml    <application>标签里配置 `tools:replace="android:allowBackup"`
  否则会报错
```
  * What went wrong:
Execution failed for task ':app:processDebugMainManifest'.
> Manifest merger failed : Attribute application@allowBackup value=(false) from AndroidManifest.xml:11:7-34
  	is also present at [com.github.yyued:SVGAPlayer-Android:2.5.15] AndroidManifest.xml:12:9-35 value=(true).
  	Suggestion: add 'tools:replace="android:allowBackup"' to <application> element at AndroidManifest.xml:7:5-12:19 to override.
```

- 因为这个插件使用kotlin写的，如果没有kotlin环境的话，需要安装下  `android/build.gradle` 中配置

```
    ext {
        ...
        kotlin_version = "1.4.0"
    }
    dependencies {
        ...
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
      } 

```

### iOS 配置
无

## JS项目配置(如果不把svga文件放js中可以跳过)
`metro.config.js` , 否则会报引用不到资源
```
module.exports = (async () => {
  const defaultAssetExts = require("metro-config/src/defaults/defaults").assetExts;
  return {
    resolver: {
      assetExts: [...defaultAssetExts, 'svga'],
    },
    transformer: {
      getTransformOptions: async () => ({
        transform: {
          experimentalImportSupport: false,
          inlineRequires: false,
        },
      }),
    },
  };
})();
```


## 使用
大概就是这样吧， svgaTag获取到实例后可以作一些暂停，清空，恢复等操作，具体看example

```
import { ISvgaProps, SVGAView } from 'react-native-svga'

// 本地
source: SVGAModule.getAssets(require('./posche.svga'))

// 网络
source: 'https://github.com/yyued/SVGA-Samples/blob/master/heartbeat.svga?raw=true'

// eg
const { width, height } = Dimensions.get('window')
const kScreenW = width
const kScreenH = height

let svgaTag: SVGAView

<SVGAView
                  ref={ref => (svgaTag = ref)}
                  source: 'https://github.com/yyued/SVGA-Samples/blob/master/heartbeat.svga?raw=true'
                  loop={1}
                  style={{ width: kScreenW, height: kScreenH }}
                  onFinished={() => {
                        console.log("finish")
                  }}
                />


// 暂停
svgaTag.pauseAnimation()

// 恢复
svgaTag.startAnimation()

...
```
