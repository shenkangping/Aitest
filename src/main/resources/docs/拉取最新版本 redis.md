# 拉取最新版本
docker pull redis:latest

# 拉取指定版本（如 7.2）
docker pull redis:7.2

# 查看已下载的镜像
docker images | grep redis


# 最简单的运行方式
docker run --name my-redis -d redis

# 映射端口到宿主机（6379）
docker run --name my-redis -p 6379:6379 -d redis


# 创建配置文件目录
mkdir -p ~/docker/redis/conf

# 创建 redis.conf 配置文件
cat > ~/docker/redis/conf/redis.conf << EOF
bind 0.0.0.0
port 6379
appendonly yes
requirepass 123456
EOF

# 运行容器并挂载配置文件
docker run --name my-redis \
  -p 6379:6379 \
  -v ~/docker/redis/conf/redis.conf:/usr/local/etc/redis/redis.conf \
  -v ~/docker/redis/data:/data \
  -d redis \
  redis-server /usr/local/etc/redis/redis.conf


  # 查看运行中的容器
docker ps

# 查看所有容器（包括停止的）
docker ps -a

# 停止容器
docker stop my-redis

# 启动容器
docker start my-redis

# 重启容器
docker restart my-redis

# 删除容器（需先停止）
docker rm my-redis

# 查看容器日志
docker logs my-redis
docker logs -f my-redis  # 实时查看



# 进入容器内部
docker exec -it my-redis sh

# 在容器内连接 Redis
redis-cli

# 或直接执行命令
docker exec -it my-redis redis-cli ping


# 使用 redis-cli（需要宿主机安装）
redis-cli -h localhost -p 6379

# 如果有密码
redis-cli -h localhost -p 6379 -a yourpassword



# Docker Compose 方式（推荐生产环境）
创建 docker-compose.yml 文件：

yaml
version: '3.8'

services:
  redis:
    image: redis:latest
    container_name: my-redis
    restart: always
    ports:
      - "6379:6379"
    volumes:
      - ./redis/data:/data
      - ./redis/conf/redis.conf:/usr/local/etc/redis/redis.conf
    command: redis-server /usr/local/etc/redis/redis.conf
    environment:
      - REDIS_PASSWORD=yourpassword
    networks:
      - redis-network

  redis-insight:  # Redis 可视化工具（可选）
    image: redis/redisinsight:latest
    container_name: redis-insight
    restart: always
    ports:
      - "5540:5540"
    volumes:
      - ./redis-insight:/data
    networks:
      - redis-network

networks:
  redis-network:
    driver: bridge
启动服务：

bash
# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down

# 停止并删除数据卷
docker-compose down -v
