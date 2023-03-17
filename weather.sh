#!/bin/bash

# 자바 프로젝트 build
echo "Building Spring Boot application..."
./gradlew build

#docker file을 참조해서 docker image 만들기
echo "Building Docker image..."
docker build -t weatherservice .

# 같은 이미지로 컨테이너를 띄워졌을 때 포트가 중복되는 것을 막기 위해서
# 같은 이름의 container는 삭제하고 weatherwhere 이미지로 container 생성
echo "Starting Docker container..."
source ./bash_profile
winpty docker rm -f weatherservice || true && winpty docker run -it --name weatherservice -p 8080:8080 weatherservice

echo "Application started!"
