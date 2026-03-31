package com.pioneer.aitest.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pioneer
 * @since 11.0
 * Created on 2026/3/31
 */
@Configuration
public class RagConfig {
    @Resource
    EmbeddingModel qwenEmbeddingModel;

    @Resource
    EmbeddingStore<TextSegment> embeddingStore;

    @Bean
    ContentRetriever contentRetriever() {
        // 1.文档加载器加载文档 documents
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/docs");  // loadDocuments 不会递归子目录
/*        System.out.println("=== 加载文档数: " + documents.size());
        documents.forEach(doc -> {
            System.out.println("文件: " + doc.metadata().getString("file_name"));
            System.out.println("内容长度: " + (doc.text() == null ? "NULL" : doc.text().length()));
            System.out.println("内容前50字: " + (doc.text() == null ? "NULL" : doc.text().substring(0, Math.min(50, doc.text().length()))));
        });*/

//        List<Document> documents = FileSystemDocumentLoader.loadDocumentsRecursively("docs/");   //递归子目录
        // 2.文档切割器切割文档 按照段落切割 每段 800字符  重叠 100字符
        DocumentByParagraphSplitter documentByParagraphSplitter = new DocumentByParagraphSplitter( 800,100);
        // 3.自定义向量存储   将文档转换成向量 并进行向量化存储，同时设置元数据等
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentByParagraphSplitter)  // 切割文档
                .textSegmentTransformer(textSegment -> {
                    String text = textSegment.text();
                    if (text == null || text.isBlank()) {
                        // 不能返回 null，直接原样返回，ingestor 内部会处理
                        return textSegment;
                    }
                    String fileName = textSegment.metadata().getString("file_name");
                    String prefix = (fileName != null && !fileName.isBlank()) ? fileName + "\n" : "";
                    return TextSegment.from(prefix + text, textSegment.metadata());
                })
//                .textSegmentTransformer(textSegment ->
//                        TextSegment.from(textSegment.metadata().getString("file_name") + "\n" + textSegment.text(), textSegment.metadata()))  // 为提高文档索引质量，设置文档碎片的新的元数据。第一个参数是文本片段信息，第二个参数是元数据信息
                .embeddingModel(qwenEmbeddingModel)  // 向量化转换
                .embeddingStore(embeddingStore)  //内存向量库
                .build();

        // 过滤掉空文档
        documents = documents.stream()
                .filter(doc -> doc.text() != null && !doc.text().isBlank())
                .collect(Collectors.toList());


        // 4.加载文档并执行上面的步骤，然后存储进向量数据库
        embeddingStoreIngestor.ingest(documents);

        // 5.自定义内容加载器 以供检索使用
        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(qwenEmbeddingModel)
                .embeddingStore(embeddingStore)
                .maxResults(5)   //返回最多 5 条件结果
                .minScore(0.75) //过滤检索分数小于 0.75的结果
                .build();
        return contentRetriever;
    }
}
