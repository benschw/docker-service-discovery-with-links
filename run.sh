#!/bin/bash


echo "Start Hipache and Redis"
docker run -d -p 6379:6379 -p 80:80 samalba/hipache supervisord -n
sleep 2 # redis doesn't seem to be ready immediately

echo "Start Memcached"
memcache_id=$(docker run -d -p 1121 benschw/memcached)
memcache_name=$(docker inspect $memcache_id | python -c 'import json,sys;obj=json.load(sys.stdin);print obj[0]["Name"]')

echo "Start App (Service role)"
service_id=$(docker run -d -p 8080 -p 8081 -link $memcache_name:memcached app:latest)

service_name=$(docker inspect $service_id | python -c 'import json,sys;obj=json.load(sys.stdin);print obj[0]["Name"]')

echo "Start App (Client role)"
client_id=$(docker run -d -p 8080 -p 8081 -link $service_name:service app:latest)


docker_bridge_ip=$(/sbin/ifconfig docker0 | sed -n '2 p' | awk '{print $2}' | cut -d":" -f2)


echo "Configure Hipache"
client_port=$(docker port $client_id 8080)
docker run -i -t samalba/hipache redis-cli -h $docker_bridge_ip -p 6379 rpush frontend:client.local client
docker run -i -t samalba/hipache redis-cli -h $docker_bridge_ip -p 6379 rpush frontend:client.local http://$docker_bridge_ip:$client_port
