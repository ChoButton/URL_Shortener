const webpack = require('webpack');

module.exports = function override(config, env) {
    config.resolve.fallback = {
        ...config.resolve.fallback,
        "stream": require.resolve("stream-browserify"),
        "crypto": require.resolve("crypto-browserify")
    };

    config.plugins.push(
        new webpack.ProvidePlugin({
            process: 'process/browser',
        })
    );
    return config;
};