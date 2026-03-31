仅安装 redis-cli（从源码编译）
如果你只想安装 redis-cli 而不安装 Redis 服务器：

bash
# 下载 Redis 源码
wget https://download.redis.io/redis-stable.tar.gz
tar xzf redis-stable.tar.gz
cd redis-stable

# 只编译 redis-cli
make redis-cli

# 安装到系统路径
sudo cp src/redis-cli /usr/local/bin/

# 验证
redis-cli --version

# 清理源码（可选）
cd ..
rm -rf redis-stable redis-stable.tar.gz


# 链接容器中或者远程的 redis
redis-cli -h 127.0.0.1 -p 6379 -a 123456