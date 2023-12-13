TIME="$(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"
CURRENT_PORT=$(cat /home/ubuntu/service-url.inc | grep -Po '[0-9]+' | tail -1)
DEPRECATED_PORT=0
DEPRECATED_STATE=""

if [ $CURRENT_PORT -eq 8081 ]; then
    DEPRECATED_PORT="8082"
    DEPRECATED_STATE="green"
elif [ $CURRENT_PORT -eq 8082 ]; then
    DEPRECATED_PORT="8081"
    DEPRECATED_STATE="blue"
else
    echo "[$TIME] > No f-it WAS is connected to nginx port $CURRENT_PORT." >> /home/ubuntu/app/deploy/deploy.log
    exit 1
fi

if [ -n "$(docker ps | grep fit-was-$DEPRECATED_STATE)" ]; then
  echo "[$TIME] > Kill f-it WAS running with state port $DEPRECATED_PORT in state $DEPRECATED_STATE." >> /home/ubuntu/app/deploy/deploy.log
  docker-compose -f /home/ubuntu/app/deploy/docker/docker-compose.was.yml stop $DEPRECATED_STATE
  docker rm fit-was-$DEPRECATED_STATE
fi