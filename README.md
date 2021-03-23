# react-native-svga

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
- 因为这个插件使用kotlin写的，如果没有kotlin环境的话，需要安装下  `app/build.gradle` 中配置
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

## JS项目配置
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
大概就是这样吧， svgaTag获取到实例后可以作一些暂停，清空，恢复等操作

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