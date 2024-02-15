TIME="$(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
CURRENT_PORT=$(cat /home/ubuntu/service-url.inc  | grep -Po '[0-9]+' | tail -1)
SOCKET_CURRENT_PORT=$(cat /home/ubuntu/socket-service-url.inc  | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0
SOCKET_TARGET_PORT=0

echo "[$TIME] > Nginx currently proxies was to ${CURRENT_PORT}, socket service to ${SOCKET_CURRENT_PORT} ." >> /home/ubuntu/app/deploy/deploy.log

if [ $CURRENT_PORT -eq 8081 ]; then
    TARGET_PORT=8082
    SOCKET_TARGET_PORT=9002
elif [ $CURRENT_PORT -eq 8082 ]; then
    TARGET_PORT=8081
    SOCKET_TARGET_PORT=9001
else
    echo "[$TIME] > No f-it was is connected to nginx port $CURRENT_PORT." >> /home/ubuntu/app/deploy/deploy.log
    exit 1
fi

echo "set \$service_url http://127.0.0.1:$TARGET_PORT;" | tee /home/ubuntu/service-url.inc
echo "set \$socket_service_url http://127.0.0.1:$SOCKET_TARGET_PORT;" | tee /home/ubuntu/socket-service-url.inc
sudo service nginx reload
echo "[$TIME] > Nginx reloaded." >> /home/ubuntu/app/deploy/deploy.log
echo "[$TIME] > Now Nginx proxies was to $TARGET_PORT, socket service to $SOCKET_TARGET_PORT." >> /home/ubuntu/app/deploy/deploy.log
