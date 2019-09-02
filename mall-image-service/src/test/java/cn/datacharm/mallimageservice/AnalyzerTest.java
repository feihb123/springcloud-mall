package cn.datacharm.mallimageservice;


import cn.datacharm.mallimageservice.util.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * description:
 * 分词器测试
 * @author Herb
 * @date 2019/09/2019-09-02
 */
public class AnalyzerTest {
    //term词项
    public void printTerm(Analyzer analyzer, String msg) {
        StringReader reader = new StringReader(msg);
        //用分词器a对reader进行分词
        //从对象中获取字符串属性
        TokenStream token = analyzer.tokenStream("test", reader);
        try {
            token.reset();
            CharTermAttribute attribute = token.getAttribute(CharTermAttribute.class);
            while (token.incrementToken()) {
                System.out.println(attribute.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void run() {
        Analyzer a1 = new StandardAnalyzer();
        Analyzer a2 = new SimpleAnalyzer();
        Analyzer a3 = new WhitespaceAnalyzer();
        Analyzer a4 = new SmartChineseAnalyzer();
        Analyzer a5 = new IKAnalyzer6x();

        String msg = "魔兽世界排队5小时，终于登上了";
        /*System.out.println("标准：*************************");
        printTerm(a1,msg);
        System.out.println("简单：*************************");
        printTerm(a2,msg);
        System.out.println("空格：*************************");
        printTerm(a3,msg);*/
        System.out.println("智能：*************************");
        printTerm(a4,msg);
        System.out.println("IK：***************************");
        printTerm(a5,msg);

    }
}
