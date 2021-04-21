/**
 * Metro configuration for React Native
 * https://github.com/facebook/react-native
 *
 * @format
 */

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
