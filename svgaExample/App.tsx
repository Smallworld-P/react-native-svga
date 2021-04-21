/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React, { useState } from 'react';
import {
  Button,
  Dimensions,
  Modal,
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
} from 'react-native';
import { SVGAModule, SVGAView } from 'react-native-svga';

import {
  Colors,
} from 'react-native/Libraries/NewAppScreen';

const svgaList_net = [
  'https://github.com/yyued/SVGA-Samples/blob/master/heartbeat.svga?raw=true',
  'https://github.com/yyued/SVGA-Samples/blob/master/EmptyState.svga?raw=true',
  'https://github.com/yyued/SVGA-Samples/blob/master/HamburgerArrow.svga?raw=true',
  'https://github.com/yyued/SVGA-Samples/blob/master/PinJump.svga?raw=true',
  'https://github.com/yyued/SVGA-Samples/blob/master/TwitterHeart.svga?raw=true',
  'https://github.com/yyued/SVGA-Samples/blob/master/Walkthrough.svga?raw=true',
  'https://github.com/yyued/SVGA-Samples/blob/master/angel.svga?raw=true',
  'https://github.com/yyued/SVGA-Samples/blob/master/halloween.svga?raw=true',
  'https://github.com/yyued/SVGA-Samples/blob/master/kingset.svga?raw=true',
  'https://github.com/yyued/SVGA-Samples/blob/master/posche.svga?raw=true',
  'https://github.com/yyued/SVGA-Samples/blob/master/rose.svga?raw=true'
]

const svgaList_native = [
  require('./asstes/svga/angel.svga'),
]
let svgaTag: SVGAView | null

const { width, height } = Dimensions.get('window')
const kScreenW = width
const kScreenH = height

const App = () => {
  console.log('app---');

  const [url, setUrl] = useState('')
  const [modal, setModal] = useState(false)

  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
    flex: 1,
  };

  function load(url: any) {
    svgaTag?.clearAnimation()
    setUrl(url)
    setModal(true)
  }

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <Text style={{ alignSelf: 'center', fontSize: 30 }}>预加载需要科学上网</Text>
      <View style={styles.btnView}>
        <Button title={'预加载全部'} onPress={() => {
          SVGAModule.advanceDownload(svgaList_net)
        }} />
        <Button title={'网络1'} onPress={() => {
          load(svgaList_net[0])
        }} />
        <Button title={'网络2'} onPress={() => {
          load(svgaList_net[1])

        }} />
        <Button title={'网络3'} onPress={() => {
          load(svgaList_net[2])
        }} />
        <Button title={'网络4'} onPress={() => {
          load(svgaList_net[3])
        }} />
        <Button title={'网络5'} onPress={() => {
          load(svgaList_net[4])
        }} />
        <Button title={'网络6'} onPress={() => {
          load(svgaList_net[5])
        }} />
        <Button title={'本地7'} onPress={() => {
          load(SVGAModule.getAssets(svgaList_native[0]))
        }} />
      </View>

      <Modal transparent={true} visible={modal} >
        <View style={{ backgroundColor: 'rgba(0,0,0,0.5)', flex: 1 }}>
          {/* 用 modal && 是为了销毁svgaview */}
          {
            modal && <SVGAView
              ref={ref => (svgaTag = ref)}
              source={url}
              loops={1}
              style={{ width: kScreenW, height: 400 }}
              onFinished={() => {
                console.log(url + ' 播放完毕')
                setModal(false)
              }}
            />
          }
        </View>


      </Modal>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  btnView: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    flexWrap: 'wrap',
    position: 'absolute',
    bottom: 50,
    width: '100%',
  },
  button1: {
    width: 100,
    height: 40,
    backgroundColor: 'white',
    borderRadius: 10,
    justifyContent: 'center',
    alignItems: 'center',
    alignSelf: 'center'
  }
});

export default App;
