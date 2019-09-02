package cn.datacharm.mallimageservice;


import cn.datacharm.mallimageservice.util.IKAnalyzer6x;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * description:
 *
 * @author Herb
 * @date 2019/09/2019-09-02
 */
public class CreateIndex {
    @Test
    public void createIndex() throws IOException {
        //准备为索引文件保存数据文件夹
        Path path = Paths.get("D:\\Courseware\\互联网架构\\课前资料\\09-Lucene\\index01");
        //将对象传递给Lucene对象使用
        Directory dir = FSDirectory.open(path);
        //封装数据Document
        Document doc1 = new Document();
        Document doc2 = new Document();
        //productName,Image,Price,Category
        // name 属性名，value属性值，store是否存储索引
        doc1.add(new TextField("productName","三星固态硬盘", Field.Store.YES));
        //StringField表示不需要对其进行分词计算 如id,url等
        doc1.add(new StringField("productImage", "www.image.com", Field.Store.YES));
        //point值只保留一个field的数字特征属性（范围查询），即既不计算分词也不存储
        doc1.add(new DoublePoint("productPrice", 5000));
        doc1.add(new TextField("productCategory","电脑硬件", Field.Store.YES));

        doc2.add(new TextField("productName","金士顿固态硬盘", Field.Store.YES));
        doc2.add(new StringField("productImage", "www.easymall.com", Field.Store.YES));
        doc2.add(new DoublePoint("productPrice", 355));
        doc2.add(new StringField("productPrice","355元", Field.Store.YES));
        //Store.YSE表示在处理document的时候需要存储索引，no表示不需要存储
        //比如类别
        doc2.add(new TextField("productCategory","电脑硬件", Field.Store.NO));

        //创建输出流对象writer
        IndexWriterConfig config = new IndexWriterConfig(new IKAnalyzer6x());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(dir, config);
        writer.addDocument(doc1);
        writer.addDocument(doc2);
        writer.commit();
    }
}
