/** @type {import('next').NextConfig} */
const nextConfig = {
  typescript: {
    ignoreBuildErrors: true,
  },
  images: {
    unoptimized: true,
  },
  async rewrites() {
    return [
      {
        source: '/login',
        destination: '/login.html',
      },
      {
        source: '/app',
        destination: '/app.html',
      },
      // PROXY VERCEL (HTTPS) -> OCI VM (HTTP)
      {
        source: '/api-oci/:path*',
        destination: 'http://163.176.134.19:8080/:path*',
      },
    ];
  },
};
export default nextConfig;
