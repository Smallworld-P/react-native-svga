# react-native-svga

## Getting started

`$ npm install react-native-svga --save`

### Mostly automatic installation

`$ react-native link react-native-svga`

## Usage
```javascript
import { ISvgaProps, SVGAView } from 'react-native-svga'


export function showSVGA(props: ISvgaProps) {
  let svgaTag: SVGAView
     return <View style={{ width: kScreenW, height: 500, backgroundColor: 'black', marginTop: 100 }}>
        <SVGAView
          ref={(ref) => svgaTag = ref}
          style={{ width: 300, height: 400 }}
          onFinished={() => {
            console.log("播放完毕");
          }}
          {...props}
        />
      </View>
}
```
