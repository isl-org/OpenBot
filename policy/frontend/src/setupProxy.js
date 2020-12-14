const {createProxyMiddleware} = require('http-proxy-middleware');

module.exports = function (app) {
    app.use(
        createProxyMiddleware('/ws', {
            target: 'http://localhost:8000',
            ws: true,
        }),
    );
    app.use(
        createProxyMiddleware([
            '/datasets',
            '/uploaded',
        ], {
            target: 'http://localhost:8000',
        }),
    );
};
