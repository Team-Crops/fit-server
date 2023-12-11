NOW_TIME="$(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"

CURRENT_PORT=$(cat /home/ubuntu/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0
TARGET_STATE = ""

if [ ${CURRENT_PORT} -eq 8081 ]; then
  CURRENT_STATE = "blue"
  TARGET_PORT=8082
  TARGET_STATE = "green"
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  CURRENT_STATE = "green"
  TARGET_PORT=8081
  TARGET_STATE = "blue"
else
  echo "[$NOW_TIME] > No f-it WAS is connected to nginx port ${CURRENT_PORT}." >> /home/ubuntu/app/deploy.log
  echo "[$NOW_TIME] > Assign current to port 8081 in blue state." >> /home/ubuntu/app/deploy.log
  CURRENT_PORT=8081
  CURRENT_STATE = "blue"
  echo "set \$service_url http://localhost:${CURRENT_PORT};" | tee /etc/nginx/conf.d/service-url.inc
  TARGET_PORT=8082
  TARGET_STATE = "green"
fi

echo "[$NOW_TIME] > Current status of running f-it WAS is port ${CURRENT_PORT} in ${CURRENT_STATE} state." >> /home/ubuntu/app/deploy.log

if [ -z $(docker ps | grep fit-was-$(TARGET_STATE)) ]; then
  echo "[$NOW_TIME] > Kill f-it WAS running with state port ${TARGET_PORT} in state $(TARGET_STATE)." >> /home/ubuntu/app/deploy.log
  docker-compose -f docker-compose.was.yml stop $(TARGET_STATE)
fi

aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 728702143069.dkr.ecr.ap-northeast-2.amazonaws.com
docker-compose -f docker-compose.was.yml pull $(TARGET_STATE)
docker-compose -f docker-compose.was.yml up -d $(TARGET_STATE)
echo "[$NOW_TIME] > Now new f-it WAS running with port ${TARGET_PORT} in ${TARGET_STATE} state." >> /home/ubuntu/app/deploy.log