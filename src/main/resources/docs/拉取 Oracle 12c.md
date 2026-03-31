# 拉取 Oracle 12c 
docker pull registry.cn-hangzhou.aliyuncs.com/zhuyijun/oracle-12c

# 运行 Oracle 12c
docker run -d \
  --name oracle12c \
  -p 1521:1521 \
  -p 8080:8080 \
  -e ORACLE_SID=ORCL \
  -e ORACLE_PWD=12345678 \
  -e ORACLE_CHARACTERSET=AL32UTF8 \
  -v oracle_data:/opt/oracle/oradata \
  registry.cn-hangzhou.aliyuncs.com/zhuyijun/oracle-12c


 #   环境变量说明
ORACLE_PWD 或 ORACLE_PASSWORD: 设置 SYS、SYSTEM 等用户的密码

ORACLE_CHARACTERSET: 数据库字符集（默认为 AL32UTF8）

APP_USER 和 APP_USER_PASSWORD: 创建应用用户（可选）


# 验证安装
#查看容器状态
docker ps

#查看容器日志
docker logs -f oracle12c

#进入容器
docker exec -it oracle12c bash

#在容器内连接数据库
sqlplus sys/your_password@//localhost:1521/XE as sysdba


 # 连接数据库
使用以下信息连接数据库：
主机: localhost
端口: 1521
服务名: ORCL
用户名: system
密码: 你设置的密码

# 常用管理命令
#启动容器
docker start oracle12c
#停止容器
docker stop oracle12c
#重启容器
docker restart oracle12c
#删除容器
docker rm oracle12c
#删除数据卷（谨慎操作！）
docker volume rm oracle_data

# 故障排除
如果遇到问题：

#查看详细日志
docker logs oracle12c
#检查容器资源使用情况
docker stats oracle12c
#进入容器调试
docker exec -it oracle12c bash



-- 查看所有 PDB,然后 sid 用 pdb 进行链接
SELECT name, open_mode FROM v$pdbs;
-- 常见 PDB 名称可能是：
-- ORCLPDB, PDBORCL, XEPDB1 等






-- 创建新用户
CREATE USER myuser IDENTIFIED BY mypassword;

-- 授予基本权限
GRANT CONNECT, RESOURCE,DBA TO myuser;

-- 授予所有权限（谨慎使用）
GRANT ALL PRIVILEGES TO myuser;

-- 授予创建会话权限
GRANT CREATE SESSION TO myuser;

-- 授予无限表空间权限
GRANT UNLIMITED TABLESPACE TO myuser;


jdbc:oracle:thin:@127.0.0.1:1521/ORCLPDB1
SYS as SYSDBA
12345678