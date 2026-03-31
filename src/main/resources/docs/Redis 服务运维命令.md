Redis 服务运维命令
以下是 Redis 服务的完整运维命令集合，涵盖启动、停止、监控、备份、优化等各个方面。

一、服务管理命令
1. 启动 Redis
bash
# 前台启动（默认配置）
redis-server

# 后台启动（使用配置文件）
redis-server /usr/local/etc/redis/redis.conf

# 指定端口启动
redis-server --port 6380

# 带密码启动
redis-server --requirepass yourpassword

# 以守护进程方式启动（需在配置文件中设置 daemonize yes）
redis-server --daemonize yes
2. 停止 Redis
bash
# 优雅停止（推荐）
redis-cli shutdown

# 带密码停止
redis-cli -a yourpassword shutdown

# 强制停止（不推荐，可能导致数据丢失）
kill -9 $(pgrep redis-server)

# 停止指定端口的 Redis
redis-cli -p 6380 shutdown

# 停止所有 Redis 实例
pkill redis-server
3. 重启 Redis
bash
# 先停止再启动
redis-cli shutdown
redis-server /usr/local/etc/redis/redis.conf

# 使用系统服务（如果配置了）
brew services restart redis  # Homebrew 安装
4. 查看 Redis 状态
bash
# 查看进程
ps aux | grep redis-server

# 查看端口监听
lsof -i :6379
netstat -an | grep 6379

# 查看服务状态
brew services list | grep redis  # Homebrew
redis-cli ping  # 返回 PONG 表示正常运行
二、连接与认证命令
bash
# 本地连接
redis-cli

# 远程连接
redis-cli -h 192.168.1.100 -p 6379

# 带密码连接
redis-cli -a yourpassword

# 连接时指定数据库
redis-cli -n 2  # 选择数据库 2

# 连接后认证
redis-cli
127.0.0.1:6379> AUTH yourpassword

# SSL 连接
redis-cli --tls --cert cert.pem --key key.pem --cacert ca.pem
三、监控与诊断命令
1. 实时监控
bash
# 实时监控所有命令
redis-cli MONITOR

# 查看慢查询
redis-cli SLOWLOG GET 10
redis-cli SLOWLOG LEN
redis-cli SLOWLOG RESET

# 查看实时统计
redis-cli INFO stats
2. 信息查看
bash
# 查看所有信息
redis-cli INFO

# 查看特定模块
redis-cli INFO server      # 服务器信息
redis-cli INFO clients     # 客户端信息
redis-cli INFO memory      # 内存信息
redis-cli INFO persistence # 持久化信息
redis-cli INFO stats       # 统计信息
redis-cli INFO replication # 复制信息
redis-cli INFO cpu         # CPU 信息
redis-cli INFO keyspace    # 键空间信息

# 查看配置
redis-cli CONFIG GET '*'
redis-cli CONFIG GET maxmemory
redis-cli CONFIG GET save

# 查看客户端连接
redis-cli CLIENT LIST
redis-cli CLIENT INFO

# 查看命令统计
redis-cli INFO commandstats
3. 性能测试
bash
# 运行基准测试
redis-benchmark -q

# 测试特定命令
redis-benchmark -t set,get -n 100000

# 测试管道
redis-benchmark -t set -P 16 -q

# 测试特定服务器
redis-benchmark -h localhost -p 6379 -a password
四、内存管理命令
bash
# 查看内存使用
redis-cli INFO memory
redis-cli MEMORY STATS

# 查看键的内存使用
redis-cli MEMORY USAGE key_name

# 查看内存碎片率
redis-cli INFO memory | grep mem_fragmentation_ratio

# 查看最大内存配置
redis-cli CONFIG GET maxmemory

# 设置最大内存
redis-cli CONFIG SET maxmemory 2gb

# 查看内存淘汰策略
redis-cli CONFIG GET maxmemory-policy

# 设置内存淘汰策略
redis-cli CONFIG SET maxmemory-policy allkeys-lru

# 手动内存清理
redis-cli MEMORY PURGE
五、持久化管理
RDB 持久化
bash
# 手动保存数据（阻塞）
redis-cli SAVE

# 后台保存（非阻塞）
redis-cli BGSAVE

# 查看上次保存时间
redis-cli LASTSAVE

# 查看 RDB 配置
redis-cli CONFIG GET save
redis-cli CONFIG GET dbfilename
redis-cli CONFIG GET dir

# 动态修改 RDB 配置
redis-cli CONFIG SET save "900 1 300 10 60 10000"
AOF 持久化
bash
# 开启 AOF
redis-cli CONFIG SET appendonly yes

# 关闭 AOF
redis-cli CONFIG SET appendonly no

# 手动重写 AOF
redis-cli BGREWRITEAOF

# 查看 AOF 配置
redis-cli CONFIG GET append*
redis-cli CONFIG GET auto-aof-rewrite*

# AOF 修复
redis-check-aof --fix appendonly.aof
六、备份与恢复
备份
bash
# 1. 生成 RDB 快照
redis-cli BGSAVE

# 2. 复制 RDB 文件
cp /usr/local/var/redis/dump.rdb /backup/dump_$(date +%Y%m%d).rdb

# 3. 备份 AOF 文件
cp /usr/local/var/redis/appendonly.aof /backup/

# 4. 远程备份
scp /usr/local/var/redis/dump.rdb user@backup-server:/backup/

# 5. 使用脚本备份
#!/bin/bash
BACKUP_DIR="/backup/redis"
DATE=$(date +%Y%m%d_%H%M%S)
redis-cli BGSAVE
sleep 5
cp /usr/local/var/redis/dump.rdb $BACKUP_DIR/dump_$DATE.rdb
echo "Backup completed: dump_$DATE.rdb"
恢复
bash
# 1. 停止 Redis
redis-cli SHUTDOWN

# 2. 替换 RDB 文件
cp /backup/dump.rdb /usr/local/var/redis/dump.rdb

# 3. 启动 Redis
redis-server /usr/local/etc/redis/redis.conf

# 4. 验证数据
redis-cli DBSIZE
七、数据库管理
bash
# 查看数据库数量
redis-cli CONFIG GET databases

# 切换数据库
redis-cli SELECT 2

# 查看当前数据库键数量
redis-cli DBSIZE

# 查看所有键
redis-cli KEYS '*'
redis-cli SCAN 0  # 使用游标，不阻塞

# 删除当前数据库所有数据
redis-cli FLUSHDB

# 删除所有数据库所有数据
redis-cli FLUSHALL

# 异步删除（不阻塞）
redis-cli FLUSHDB ASYNC
redis-cli FLUSHALL ASYNC

# 查看键类型
redis-cli TYPE key_name

# 查看键过期时间
redis-cli TTL key_name
redis-cli PTTL key_name  # 毫秒
八、集群管理
单机集群模式
bash
# 创建集群
redis-cli --cluster create 127.0.0.1:7000 127.0.0.1:7001 \
  127.0.0.1:7002 127.0.0.1:7003 --cluster-replicas 1

# 查看集群信息
redis-cli -c CLUSTER INFO
redis-cli -c CLUSTER NODES

# 添加节点
redis-cli --cluster add-node 127.0.0.1:7004 127.0.0.1:7000

# 删除节点
redis-cli --cluster del-node 127.0.0.1:7000 <node-id>

# 重新分片
redis-cli --cluster reshard 127.0.0.1:7000

# 检查集群
redis-cli --cluster check 127.0.0.1:7000
哨兵模式
bash
# 查看哨兵信息
redis-cli -p 26379 SENTINEL masters
redis-cli -p 26379 SENTINEL slaves mymaster
redis-cli -p 26379 SENTINEL get-master-addr-by-name mymaster

# 故障转移
redis-cli -p 26379 SENTINEL failover mymaster

# 重置哨兵
redis-cli -p 26379 SENTINEL reset mymaster
九、主从复制管理
bash
# 查看复制状态
redis-cli INFO replication

# 设置主服务器
redis-cli SLAVEOF 192.168.1.100 6379

# 取消复制（提升为主）
redis-cli SLAVEOF NO ONE

# 查看复制延迟
redis-cli INFO replication | grep lag

# 手动全量同步
redis-cli SLAVEOF NO ONE
redis-cli SLAVEOF 192.168.1.100 6379
十、日志管理
bash
# 查看日志级别
redis-cli CONFIG GET loglevel

# 修改日志级别
redis-cli CONFIG SET loglevel debug  # debug, verbose, notice, warning

# 查看日志文件位置
redis-cli CONFIG GET logfile

# 查看日志（根据配置路径）
tail -f /usr/local/var/log/redis/redis.log

# 日志轮转配置（logrotate）
cat > /etc/logrotate.d/redis << EOF
/usr/local/var/log/redis/redis.log {
    daily
    rotate 7
    missingok
    notifempty
    compress
    postrotate
        redis-cli CONFIG SET loglevel warning
    endscript
}
EOF
十一、安全检查
bash
# 修改密码
redis-cli CONFIG SET requirepass "newpassword"
redis-cli AUTH newpassword

# 禁用危险命令
redis-cli CONFIG SET rename-command FLUSHDB ""
redis-cli CONFIG SET rename-command FLUSHALL ""
redis-cli CONFIG SET rename-command CONFIG ""

# 绑定特定 IP
redis-cli CONFIG SET bind "127.0.0.1 10.0.0.1"

# 设置保护模式
redis-cli CONFIG SET protected-mode yes

# 查看所有安全配置
redis-cli CONFIG GET "*"
十二、性能优化命令
bash
# 查看慢查询
redis-cli SLOWLOG GET 10

# 设置慢查询阈值
redis-cli CONFIG SET slowlog-log-slower-than 10000  # 微秒

# 设置慢查询最大长度
redis-cli CONFIG SET slowlog-max-len 128

# 查看客户端缓冲区
redis-cli CLIENT LIST | grep -E "omem|obl"

# 优化 TCP backlog
redis-cli CONFIG SET tcp-backlog 511

# 查看连接数
redis-cli INFO stats | grep connected_clients
十三、紧急处理命令
bash
# 强制写入磁盘
redis-cli SAVE

# 清空所有数据（危险）
redis-cli FLUSHALL

# 查看大 key
redis-cli --bigkeys

# 查看热点 key
redis-cli --hotkeys

# 查看延迟
redis-cli --latency
redis-cli --latency-history
redis-cli --latency-dist

# 检测内存问题
redis-cli --memkeys
