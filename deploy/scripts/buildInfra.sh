source /etc/environment
TIME="$(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"


if [ -n "$(docker ps | grep fit-mariadb)" ]; then
  echo "[$TIME] > Start running f-it Mariadb." >> /home/ubuntu/app/deploy/deploy.log
  docker-compose -f /home/ubuntu/app/deploy/docker/docker-compose.infra.yml pull mariadb \
  && docker-compose -f /home/ubuntu/app/deploy/docker/docker-compose.infra.yml up -d mariadb
else
  echo "[$TIME] > f-it Redis is already running." >> /home/ubuntu/app/deploy/deploy.log
fi

if [ -z "$(docker ps | grep fit-redis)" ]; then # redis 가 돌아가지 않고 있음
  echo "[$TIME] > Start running f-it Redis." >> /home/ubuntu/app/deploy/deploy.log
  docker-compose -f /home/ubuntu/app/deploy/docker/docker-compose.infra.yml pull redis \
  && docker-compose -f /home/ubuntu/app/deploy/docker/docker-compose.infra.yml up -d redis
else
  echo "[$TIME] > f-it Redis is already running." >> /home/ubuntu/app/deploy/deploy.log
fi