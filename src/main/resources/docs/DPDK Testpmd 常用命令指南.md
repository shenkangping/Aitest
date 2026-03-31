# DPDK Testpmd 常用命令指南

> 本文档基于 DPDK 22.11 版本整理，涵盖 testpmd 的常用启动参数和运行时命令。

## 目录

- [简介](#简介)
- [启动命令格式](#启动命令格式)
- [EAL 通用参数](#eal-通用参数)
- [Testpmd 应用参数](#testpmd-应用参数)
- [运行时命令](#运行时命令)
- [完整示例](#完整示例)

---

## 简介

Testpmd 是 DPDK 提供的参考应用程序，主要用于：

- 在网络接口的以太网端口之间转发数据包
- 测试 DPDK PMD (Poll Mode Driver) 的各项功能
- 验证 RSS、过滤器、Flow Director 等特性

---

## 启动命令格式

Testpmd 命令行分为两部分，用 `--` 分隔：

```bash
./dpdk-testpmd <EAL参数> -- <Testpmd参数>
```

**基本示例：**

```bash
sudo ./dpdk-testpmd -l 0-3 -n 4 -- -i --portmask=0x1 --nb-cores=2
```

---

## EAL 通用参数

EAL (Environment Abstraction Layer) 参数用于配置 DPDK 运行环境。

| 参数 | 说明 | 示例 |
|------|------|------|
| `-c <核心掩码>` | 指定运行的 CPU 核心（十六进制掩码） | `-c 0xf`（使用核心 0-3） |
| `-l <核心列表>` | 指定运行的 CPU 核心（列表格式） | `-l 0-3` 或 `-l 0,1,2,3` |
| `-n <通道数>` | 内存通道数量 | `-n 4` |
| `-a <PCI地址>` | 允许使用的设备（白名单） | `-a 0000:11:00.0` |
| `-b <PCI地址>` | 禁止使用的设备（黑名单） | `-b 0000:11:00.1` |
| `--socket-mem <内存>` | 每个 NUMA 节点预分配的内存(MB) | `--socket-mem=1024,0` |
| `--file-prefix <前缀>` | 共享内存文件前缀（多实例时使用） | `--file-prefix=testpmd1` |
| `--proc-type <类型>` | 进程类型 (primary/secondary/auto) | `--proc-type=auto` |
| `--huge-dir <路径>` | 大页内存挂载点 | `--huge-dir=/mnt/huge` |

### EAL 参数示例

```bash
# 使用核心 2-11，4 个内存通道，指定网卡
sudo ./dpdk-testpmd -l 2-11 -n 4 -a 0000:11:00.0 --socket-mem=1024,0 -- -i

# 多实例运行，使用不同的文件前缀
sudo ./dpdk-testpmd -l 0-3 -n 4 --file-prefix=instance1 -- -i
sudo ./dpdk-testpmd -l 4-7 -n 4 --file-prefix=instance2 -- -i
```

---

## Testpmd 应用参数

### 基本参数

| 参数 | 说明 | 默认值 |
|------|------|--------|
| `-i, --interactive` | 交互模式运行 | 否 |
| `-a, --auto-start` | 自动启动转发 | 否 |
| `--tx-first` | 先发送一批包再开始转发 | 否 |
| `--portmask=<掩码>` | 使用的端口掩码 | 所有端口 |
| `--nb-cores=<数量>` | 转发使用的核心数 | 1 |
| `--nb-ports=<数量>` | 使用的端口数 | 所有可用端口 |

### 队列配置

| 参数 | 说明 | 默认值 |
|------|------|--------|
| `--rxq=<N>` | 每端口 RX 队列数 | 1 |
| `--txq=<N>` | 每端口 TX 队列数 | 1 |
| `--rxd=<N>` | RX 描述符数量 | 128 |
| `--txd=<N>` | TX 描述符数量 | 512 |
| `--burst=<N>` | 每次收发的包数量 | 32 |

### 转发模式

| 参数 | 说明 |
|------|------|
| `--forward-mode=io` | I/O 模式，直接转发（最快，默认） |
| `--forward-mode=mac` | MAC 模式，修改源/目的 MAC 地址 |
| `--forward-mode=macswap` | MAC 交换模式，交换源和目的 MAC |
| `--forward-mode=rxonly` | 仅接收模式，不发送 |
| `--forward-mode=txonly` | 仅发送模式，不接收 |
| `--forward-mode=csum` | 校验和模式，验证/重新计算校验和 |
| `--forward-mode=icmpecho` | ICMP 回显模式，响应 ping 请求 |
| `--forward-mode=flowgen` | 流生成模式，生成多种目的 IP 的流 |
| `--forward-mode=5tswap` | 五元组交换模式 |
| `--forward-mode=noisy` | 噪声邻居模式，模拟内存访问压力 |

### 端口拓扑

| 参数 | 说明 |
|------|------|
| `--port-topology=paired` | 成对转发：(0,1), (2,3)...（默认） |
| `--port-topology=chained` | 链式转发：0→1→2→... |
| `--port-topology=loop` | 环回模式：每个端口发回自己 |

### 其他常用参数

| 参数 | 说明 |
|------|------|
| `--eth-peer=<端口>,<MAC>` | 设置指定端口的目的 MAC 地址 |
| `--disable-rss` | 禁用 RSS |
| `--rss-ip` | 基于 IP 的 RSS |
| `--rss-udp` | 基于 UDP 的 RSS |
| `--enable-rx-cksum` | 启用 RX 校验和卸载 |
| `--enable-lro` | 启用大接收卸载 (LRO) |
| `--max-pkt-len=<N>` | 最大包长度 |
| `--mbuf-size=<N>` | mbuf 数据区大小 |

---

## 运行时命令

进入交互模式后（使用 `-i` 参数），可以使用以下命令。

### 帮助命令

```bash
# 显示所有帮助
testpmd> help all

# 显示特定类别帮助
testpmd> help control    # 控制命令
testpmd> help display    # 显示命令
testpmd> help config     # 配置命令
testpmd> help ports      # 端口命令
testpmd> help filters    # 过滤器命令
```

### 控制命令

```bash
# 启动转发
testpmd> start

# 启动转发，先发送一批包
testpmd> start tx_first

# 停止转发
testpmd> stop

# 退出 testpmd
testpmd> quit
```

### 显示命令

```bash
# 显示端口信息
testpmd> show port info all
testpmd> show port info 0

# 显示端口统计信息
testpmd> show port stats all
testpmd> show port stats 0

# 显示扩展统计信息 (xstats)
testpmd> show port xstats all
testpmd> show port xstats 0

# 清除统计信息
testpmd> clear port stats all
testpmd> clear port stats 0

# 显示转发配置
testpmd> show config fwd

# 显示 RX/TX 队列信息
testpmd> show rxq info 0 0    # 端口0的RX队列0
testpmd> show txq info 0 0    # 端口0的TX队列0

# 显示端口支持的功能
testpmd> show port cap 0

# 显示当前转发模式
testpmd> show fwd mode
```

### 配置命令

```bash
# 设置转发模式
testpmd> set fwd io
testpmd> set fwd mac
testpmd> set fwd rxonly
testpmd> set fwd txonly

# 设置详细级别
testpmd> set verbose 1

# 设置突发大小
testpmd> set burst 64

# 设置混杂模式
testpmd> set promisc all on
testpmd> set promisc 0 off

# 设置 MAC 地址
testpmd> set eth-peer 0 00:11:22:33:44:55

# 设置 RX/TX 队列数（需要先停止端口）
testpmd> port stop all
testpmd> port config all rxq 4
testpmd> port config all txq 4
testpmd> port start all
```

### 端口管理命令

```bash
# 停止端口
testpmd> port stop 0
testpmd> port stop all

# 启动端口
testpmd> port start 0
testpmd> port start all

# 关闭端口
testpmd> port close 0

# 热插拔：卸载端口
testpmd> port stop 0
testpmd> port close 0
testpmd> port detach 0

# 热插拔：加载端口
testpmd> port attach 0000:11:00.0

# 重置端口
testpmd> port reset 0
```

### 流规则命令 (Flow API)

```bash
# 创建流规则：将目的 IP 192.168.1.1 的流量转发到队列 1
testpmd> flow create 0 ingress pattern eth / ipv4 dst is 192.168.1.1 / end actions queue index 1 / end

# 创建流规则：丢弃特定端口的 UDP 流量
testpmd> flow create 0 ingress pattern eth / ipv4 / udp dst is 4789 / end actions drop / end

# 列出流规则
testpmd> flow list 0

# 查询流规则统计
testpmd> flow query 0 0 count

# 删除流规则
testpmd> flow destroy 0 rule 0

# 删除所有流规则
testpmd> flow flush 0

# 验证流规则（不实际创建）
testpmd> flow validate 0 ingress pattern eth / ipv4 / end actions queue index 0 / end

# 从文件加载流规则
testpmd> load /path/to/flow-rules.txt
```

### 镜像规则命令

```bash
# 设置 VLAN 镜像规则
testpmd> set port 0 mirror-rule 0 vlan-mirror 0,1 dst-pool 0 on

# 设置入站流量镜像
testpmd> set port 0 mirror-rule 0 uplink-mirror dst-pool 1 on

# 设置出站流量镜像
testpmd> set port 0 mirror-rule 0 downlink-mirror dst-pool 1 on

# 重置镜像规则
testpmd> reset port 0 mirror-rule 0
```

### RSS 配置命令

```bash
# 显示 RSS 配置
testpmd> show port 0 rss-hash

# 设置 RSS 哈希类型
testpmd> port config 0 rss-hash-key ipv4 <key>

# 设置 RSS 重定向表
testpmd> port config 0 rss reta (hash,queue)[,(hash,queue)]
```

---

## 完整示例

### 示例 1：基本性能测试

```bash
# 使用 4 个核心，双端口 I/O 转发
sudo ./dpdk-testpmd -l 0-4 -n 4 -- -i \
    --portmask=0x3 \
    --nb-cores=4 \
    --rxq=4 \
    --txq=4

# 启动后运行
testpmd> start
# 等待一段时间后查看统计
testpmd> show port stats all
testpmd> stop
```

### 示例 2：MAC 转发测试

```bash
# 使用 MAC 转发模式，设置目的 MAC
sudo ./dpdk-testpmd -l 0-3 -n 4 -a 0000:11:00.0 -- -i \
    --forward-mode=mac \
    --eth-peer=0,68:05:ca:c1:c9:29 \
    --nb-cores=2

testpmd> start
```

### 示例 3：单端口发包测试

```bash
# 仅发送模式，用于测试发包性能
sudo ./dpdk-testpmd -l 0-2 -n 4 -- -i \
    --portmask=0x1 \
    --nb-cores=1 \
    --forward-mode=txonly \
    --txd=2048

testpmd> start
testpmd> show port stats 0
```

### 示例 4：单端口收包测试

```bash
# 仅接收模式，用于测试收包性能
sudo ./dpdk-testpmd -l 0-2 -n 4 -- -i \
    --portmask=0x1 \
    --nb-cores=1 \
    --forward-mode=rxonly \
    --rxd=2048

testpmd> start
testpmd> show port stats 0
```

### 示例 5：多队列 RSS 测试

```bash
# 配置 4 个 RX/TX 队列，启用 RSS
sudo ./dpdk-testpmd -l 0-5 -n 4 -- -i \
    --portmask=0x3 \
    --nb-cores=4 \
    --rxq=4 \
    --txq=4 \
    --rss-ip

testpmd> start
testpmd> show port 0 rss-hash
```

### 示例 6：流规则测试

```bash
sudo ./dpdk-testpmd -l 0-3 -n 4 -- -i --rxq=4 --txq=4

# 进入交互模式后
testpmd> flow create 0 ingress pattern eth / ipv4 dst is 10.0.0.1 / end actions queue index 1 / end
testpmd> flow create 0 ingress pattern eth / ipv4 dst is 10.0.0.2 / end actions queue index 2 / end
testpmd> flow list 0
testpmd> start
```

### 示例 7：多进程模式

```bash
# 主进程
sudo ./dpdk-testpmd -l 0-1 -n 4 --proc-type=primary -- -i \
    --rxq=4 --txq=4 --num-procs=2 --proc-id=0

# 从进程（另一个终端）
sudo ./dpdk-testpmd -l 2-3 -n 4 --proc-type=secondary -- -i \
    --rxq=4 --txq=4 --num-procs=2 --proc-id=1
```

---

## 常见问题排查

### 查看端口状态

```bash
testpmd> show port info all
```

### 检查链路状态

```bash
testpmd> show port info 0
# 查看 Link status: up/down
```

### 检查丢包

```bash
testpmd> show port stats all
# 查看 RX-errors, TX-errors, RX-dropped
```

### 重置统计信息

```bash
testpmd> clear port stats all
```

---

## 参考链接

- [DPDK Testpmd 用户指南](https://doc.dpdk.org/guides/testpmd_app_ug/)
- [Testpmd 运行时函数](https://doc.dpdk.org/guides/testpmd_app_ug/testpmd_funcs.html)
- [运行 Testpmd 应用](https://doc.dpdk.org/guides/testpmd_app_ug/run_app.html)
- [Intel 性能测试指南](https://www.intel.com/content/www/us/en/developer/articles/technical/testing-dpdk-performance-and-features-with-testpmd.html)
