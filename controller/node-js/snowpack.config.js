const proxy = import('http2-proxy')

/** @type {import("snowpack").SnowpackUserConfig } */
module.exports = {
  mount: {
    "client": "/"
  },
  optimize: {
    bundle: true,
    minify: true,
    target: 'es2018',
    treeshake: true,
  },
  routes: [
    {
      src: '/ws',
      upgrade: (req, socket, head) => {

        const defaultWSHandler = (err, req, socket, head) => {
          if (err) {
            console.error('proxy error', err);
            socket.destroy();
          }
        };

        proxy.ws(
          req,
          socket,
          head,
          {
            hostname: 'localhost',
            port: 7071,
          },
          defaultWSHandler,
        );
      },
    },
  ],  
};