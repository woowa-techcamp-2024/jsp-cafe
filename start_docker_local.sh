# Docker 이미지 이름과 컨테이너 이름 설정
./gradlew build

IMAGE_NAME="jsp-cafe-image"
CONTAINER_NAME="jsp-cafe-container"

# 도커 컨테이너가 이미 실행 중인 경우 중지하고 제거합니다.
docker stop $CONTAINER_NAME || true
docker rm $CONTAINER_NAME || true

# 기존 도커 이미지 삭제
docker rmi $IMAGE_NAME || true

# 도커 이미지 빌드
docker build -t choicco89/$IMAGE_NAME --no-cache .


# 도커 컨테이너를 실행합니다.
docker run -d \
  --name $CONTAINER_NAME \
  -e HOST_IP=host.docker.internal \
  -p 8080:8080 \
  $IMAGE_NAME