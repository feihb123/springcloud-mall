package cn.datacharm.lucene;

import cn.datacharm.utils.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.nio.file.Paths;

/**
 * description:
 * 测试大量的查询功能
 *
 * @author Herb
 * @date 2019/09/2019-09-02
 */
public class SearchIndex {
    /**
     * 词项查询，利用传递的参数封装词项
     * 对分词结果获取指定的document集合
     */
    @Test
    public void termQuery() throws Exception {
        //指定文件夹索引位置
        Directory dir = FSDirectory.open(Paths.get("D:\\Courseware\\互联网架构\\课前资料\\09-Lucene\\index01"));
        //根据文件夹位置生成Reader流 创建搜索对象
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher search = new IndexSearcher(reader);
        //准备搜索条件
        Term term = new Term("productName", "硬盘");
        Query query = new TermQuery(term);
        //搜索封装了document大量表示数据的对象 （没有源数据）
        //document 评分等
        //词项中，根据字符串匹配长度，长度越大，评分越高
        //查询的条数一共多少条等等

        //search(query, result)query查询条件 result 表示查询前几条
        TopDocs topDoc = search.search(query, 10);
        System.out.println("最高分" + topDoc.getMaxScore());
        System.out.println("一共获取数据" + topDoc.totalHits);
        //利用浅查询得到评分对象拿到documentId
        ScoreDoc[] scoreDocs = topDoc.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            //每次循环都获取返回结果中的一个document评分相关内容
            System.out.println("当前Id" + scoreDoc.doc);
            System.out.println("当前doc评分" + scoreDoc.score);
            //利用documentID获取源数据 拿不到store.no的数据
            Document doc = search.doc(scoreDoc.doc);
            //解析所有属性
            System.out.println("productName" + doc.get("productName"));
            System.out.println("productImage" + doc.get("productImage"));

        }

    }

    @Test
    public void MultiQuery() throws Exception {
        //指定文件夹索引位置
        Directory dir = FSDirectory.open(Paths.get("D:\\Courseware\\互联网架构\\课前资料\\09-Lucene\\index01"));
        //根据文件夹位置生成Reader流 创建搜索对象
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher search = new IndexSearcher(reader);
        //准备搜索条件
        String[] fields = {"productName", "productCategory"};
        Analyzer analyzer = new IKAnalyzer6x();
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);
        //利用解析器生成多余查询条件
        Query query = parser.parse("三星固态硬盘是否会爆炸");

        TopDocs topDoc = search.search(query, 10);
        System.out.println("最高分" + topDoc.getMaxScore());
        System.out.println("一共获取数据" + topDoc.totalHits);
        //利用浅查询得到评分对象拿到documentId
        ScoreDoc[] scoreDocs = topDoc.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            //每次循环都获取返回结果中的一个document评分相关内容
            System.out.println("当前Id" + scoreDoc.doc);
            System.out.println("当前doc评分" + scoreDoc.score);
            //利用documentID获取源数据 拿不到store.no的数据
            Document doc = search.doc(scoreDoc.doc);
            //解析所有属性
            System.out.println("productName" + doc.get("productName"));
            System.out.println("productImage" + doc.get("productImage"));

        }

    }

    @Test
    public void booleanQuery() throws Exception {
        //指定文件夹索引位置
        Directory dir = FSDirectory.open(Paths.get("D:\\Courseware\\互联网架构\\课前资料\\09-Lucene\\index01"));
        //根据文件夹位置生成Reader流 创建搜索对象
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher search = new IndexSearcher(reader);
        //准备boolean条件的子条件termQuery
        Query query1 = new TermQuery(new Term("productName", "硬盘"));
        Query query2 = new TermQuery(new Term("productName", "三星"));
        //利用query1 2封装子条件
        BooleanClause bc1 = new BooleanClause(query1, BooleanClause.Occur.MUST);
        BooleanClause bc2 = new BooleanClause(query2, BooleanClause.Occur.MUST);
        Query query = new BooleanQuery.Builder().add(bc1).add(bc2).build();

        TopDocs topDoc = search.search(query, 10);
        System.out.println("最高分" + topDoc.getMaxScore());
        System.out.println("一共获取数据" + topDoc.totalHits);
        //利用浅查询得到评分对象拿到documentId
        ScoreDoc[] scoreDocs = topDoc.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            //每次循环都获取返回结果中的一个document评分相关内容
            System.out.println("当前Id" + scoreDoc.doc);
            System.out.println("当前doc评分" + scoreDoc.score);
            //利用documentID获取源数据 拿不到store.no的数据
            Document doc = search.doc(scoreDoc.doc);
            //解析所有属性
            System.out.println("productName" + doc.get("productName"));
            System.out.println("productImage" + doc.get("productImage"));

        }
    }

    @Test
    public void RangeQuery() throws Exception {
        //指定文件夹索引位置
        Directory dir = FSDirectory.open(Paths.get("D:\\Courseware\\互联网架构\\课前资料\\09-Lucene\\index01"));
        //根据文件夹位置生成Reader流 创建搜索对象
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher search = new IndexSearcher(reader);

        Query query = DoublePoint.newRangeQuery("productPrice", 555, 8000);

        TopDocs topDoc = search.search(query, 10);
        System.out.println("最高分" + topDoc.getMaxScore());
        System.out.println("一共获取数据" + topDoc.totalHits);
        //利用浅查询得到评分对象拿到documentId
        ScoreDoc[] scoreDocs = topDoc.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            //每次循环都获取返回结果中的一个document评分相关内容
            System.out.println("当前Id" + scoreDoc.doc);
            System.out.println("当前doc评分" + scoreDoc.score);
            //利用documentID获取源数据 拿不到store.no的数据
            Document doc = search.doc(scoreDoc.doc);
            //解析所有属性
            System.out.println("productName" + doc.get("productName"));
            System.out.println("productImage" + doc.get("productImage"));
            System.out.println("productPrice" + doc.get("productPrice"));
        }
    }
    //模糊查询 Query query = new FuzzyQuery(new Term("productName","xx"))

    //通配符查询 Query query = new WildcartQuery(new Term("productName","xx"))

}
