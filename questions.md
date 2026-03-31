- **1.log.info(res)报错** </br>
解决：
![img.png](mdImages/img.png)
##
- **2.MessageWindowChatMemory不生效**
![img_1.png](mdImages/img_1.png)
解决：</br>
![img.png](mdImages/img2.png)
##
- **3.com.pioneer.aitest.rag.RagConfig.contentRetriever创建的时候失败，spring报错 
Error creating bean with name 'agentAIServicesFactory': Injection of resource dependencies failed.................
Caused by: java.lang.IllegalArgumentException: text cannot be null or blank**</br>
解决：</br>
  FileSystemDocumentLoader.loadDocuments("src/main/resources/docs");输出加载的文档时发现其中有个 mac 系统自带的文件: </br>
  .DS_Store</br>
  内容长度: 6148</br>
  内容前50字: Bud1	     ← 二进制内容</br>

  .DS_Store 是 macOS 自动生成的二进制文件，DocumentByParagraphSplitter 切割二进制内容时会产生无法处理的空段落，导致崩溃
