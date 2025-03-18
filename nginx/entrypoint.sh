#!/bin/sh

echo "Starting Nginx with temporary configuration..."
cp /etc/nginx/temporary.conf /etc/nginx/nginx.conf
nginx -g 'daemon off;' &

echo "Waiting for Certbot to generate certificates..."
while [ ! -f /etc/letsencrypt/live/posttopurchase.store/fullchain.pem ]; do
  sleep 5
done

echo "Certificates found! Switching to HTTPS configuration..."
cp /etc/nginx/default.conf /etc/nginx/nginx.conf

echo "Reloading Nginx with secure HTTPS configuration..."
nginx -s reload

# Keep container running
wait
