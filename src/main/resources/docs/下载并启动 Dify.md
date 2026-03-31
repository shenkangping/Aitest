下载并启动 Dify
打开终端，执行以下命令克隆 Dify 的代码仓库，并启动所有服务。

bash
# 1. 克隆 Dify 代码到本地
git clone https://github.com/langgenius/dify.git

# 2. 进入 docker 目录
cd dify/docker

# 3. 复制环境变量配置文件
cp .env.example .env

# 4. 启动 Docker 容器
docker compose up -d
首次启动需要拉取镜像，请耐心等待。看到所有容器状态为 Started 或 done 即表示成功 。

🚀 第三步：访问并初始化
访问地址：在浏览器打开 http://localhost 。

设置管理员：首次进入页面，系统会引导你设置管理员邮箱和密码，按提示完成即可 。

🔑 关键一步：如何连接 AI 模型？
部署完成后，Dify 只是一个空壳，你需要接入一个 AI 模型（如 DeepSeek、GPT）才能让它开始工作。

方式一：使用本地模型（推荐，免费）
配合 Ollama 使用是最佳选择。你只需在 Mac 上安装 Ollama 并下载模型（例如 ollama run deepseek-r1:7b），然后在 Dify 的设置中找到“模型供应商” -> “Ollama”，填入地址 http://host.docker.internal:11434 即可对接 。

方式二：使用云端 API（直接）
如果你有 OpenAI、Anthropic 或其他云厂商的 API Key，可以直接在 Dify 的“模型供应商”页面填入对应的 Key，即可立即使用 。

🛠️ 常用维护命令
以后使用 Dify 时，可以在终端通过以下命令管理服务：

停止服务：docker compose down

重启服务：docker compose restart

查看日志：docker compose logs -f

配置建议：如果你的 Mac 内存只有 8GB，建议在 Docker Desktop 的设置里将资源限制调整为 4GB 以上，以保证 Dify 运行流畅 。