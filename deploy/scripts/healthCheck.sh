TIME="$(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"

CURRENT_PORT=$(cat /home/ubuntu/service-url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

if [ ${CURRENT_PORT} -eq 8081 ]; then
    TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
    TARGET_PORT=8081
else
    echo "[$TIME] > No f-it WAS is connected to nginx port ${CURRENT_PORT}." >> /home/ubuntu/app/deploy/deploy.log
    exit 1
fi

echo "[$TIME] > Start health check of f-it WAS at 'http://127.0.0.1:${TARGET_PORT}'..." >> /home/ubuntu/app/deploy/deploy.log

for RETRY_COUNT in {1..10}
do
    echo "[$TIME] > #${RETRY_COUNT} trying..." >> /home/ubuntu/app/deploy/deploy.log
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://127.0.0.1:${TARGET_PORT}/actuator/health)
    if [ ${RESPONSE_CODE} -eq 200 ]; then
        echo "[$TIME] > New f-it WAS successfully running." >> /home/ubuntu/app/deploy/deploy.log
        exit 0
    elif [ ${RETRY_COUNT} -eq 10 ]; then
        echo "[$TIME] > Health check failed." >> /home/ubuntu/app/deploy/deploy.log
        exit 1
    fi
    sleep 10
done