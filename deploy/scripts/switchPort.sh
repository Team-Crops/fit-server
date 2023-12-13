NOW_TIME="$(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"

CURRENT_PORT=$(cat /home/ubuntu/service-url.inc  | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "[$NOW_TIME] > Nginx currently proxies to ${CURRENT_PORT}." >> /home/ubuntu/app/deploy/deploy.log

if [ $CURRENT_PORT -eq 8081 ]; then
    TARGET_PORT=8082
elif [ $CURRENT_PORT -eq 8082 ]; then
    TARGET_PORT=8081
else
    echo "[$NOW_TIME] > No f-it was is connected to nginx port $CURRENT_PORT." >> /home/ubuntu/app/deploy/deploy.log
    exit 1
fi

echo "set \$service_url http://localhost:$TARGET_PORT;" | tee /home/ubuntu/service-url.inc
sudo service nginx reload
echo "[$NOW_TIME] > Nginx reloaded." >> /home/ubuntu/app/deploy/deploy.log
echo "[$NOW_TIME] > Now Nginx proxies to $TARGET_PORT." >> /home/ubuntu/app/deploy/deploy.log
