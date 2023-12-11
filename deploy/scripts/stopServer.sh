NOW_TIME="$(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"

CURRENT_PORT=$(cat /home/ubuntu/service-url.inc | grep -Po '[0-9]+' | tail -1)
DEPRECATED_PORT=0
DEPRECATED_STATE = ""

if [ ${CURRENT_PORT} -eq 8081 ]; then
    DEPRECATED_PORT = "8082"
    DEPRECATED_STATE = "green"
elif [ ${CURRENT_PORT} -eq 8082 ]; then
    DEPRECATED_PORT = "8081"
    DEPRECATED_STATE = "blue"
else
    echo "[$NOW_TIME] > No f-it WAS is connected to nginx port ${CURRENT_PORT}." >> /home/ubuntu/app/deploy.log
    exit 1
fi

if [ -z $(docker ps | grep fit-was-$(DEPRECATED_STATE)) ]; then
  echo "[$NOW_TIME] > Kill f-it WAS running with state port ${DEPRECATED_PORT} in state $(DEPRECATED_STATE)." >> /home/ubuntu/app/deploy.log
  docker-compose -f docker-compose.was.yml stop $(DEPRECATED_STATE)
  docker rm fit-was-$(DEPRECATED_STATE)
fi