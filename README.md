Client : https://heezy.dev/
API: https://api.heezy.dev/

docker build --build-arg JAR_FILE=build/libs/\*.jar -t user_security_service:latest .
docker run --name user_security_service -d -p 8080:8080 user_security_service:latest