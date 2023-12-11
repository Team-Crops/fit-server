NOW_TIME="$(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)"

echo "[$NOW_TIME] > Hello F-IT Deploy" >> /home/ubuntu/app/deploy.log
echo "1 : $(ECR_REGISTRY)" >> /home/ubuntu/app/deploy.log
echo "2 : [$ECR_REGISTRY]" >> /home/ubuntu/app/deploy.log