package cn.datacharm.mallsearchservice;

import cn.datacharm.pojo.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Map;

/**
 * description:
 * java连接es集群测试
 *
 * @author Herb
 * @date 2019/09/2019-09-05
 */
public class TransportClientTest {
    /**
     * 构造一个连接对象整合技术到springboot初始化方法
     */
    private TransportClient client;

    @Before
    public void conn() throws Exception {
        //setting是接连使用的初始化配置
        //类似GenericObjectPoolConfig
        Settings setting =Settings.builder().put("cluster.name","elasticsearch").build();
        client = new PreBuiltTransportClient(setting);
        //预构造的对象  所有方法都是基于Http协议的
        //构造request发送请求 节后response
        //发送请求之前的 代码阶段为Pre

        //连接的节点信息
        //底层Tcp连接
        InetAddress es1 = InetAddress.getByName("10.42.108.48");
        InetAddress es2 = InetAddress.getByName("10.42.16.145");
        InetAddress es3 = InetAddress.getByName("10.42.124.62");
        //扩展功能中es给不同语言连接使用的底层接口9300
        TransportAddress address1 = new InetSocketTransportAddress(es1, 9300);
        TransportAddress address2 = new InetSocketTransportAddress(es2, 9300);
        TransportAddress address3 = new InetSocketTransportAddress(es3, 9300);
        client.addTransportAddress(address1);
        client.addTransportAddress(address2);
        client.addTransportAddress(address3);
    }

    @Test
    public void test01() {
        //索引的管理
        //预处理过程中 需要利用client拿到一个管理索引的对象admin
        IndicesAdminClient admin = client.admin().indices();
        //admin调用方法访问es
        CreateIndexRequestBuilder request = admin.prepareCreate("index09");
        //response接受的响应体
        CreateIndexResponse response = request.get();
        //response接受的响应体  {"ack":,"shards_ack":""}
        System.out.println(response.isAcknowledged());
        System.out.println(response.isShardsAcked());
        System.out.println(response.remoteAddress().toString());

        //判断索引是否存在
        //admin.prepareExists("index01", "index02", "index03");

    }
    //文档数据的创建(默认的mapping)
    @Test
    public void test02() throws Exception {
        //Product对象是从数据库读取
        Product p = new Product();
        p.setProductId("product1");
        p.setProductCategory("手机");
        p.setProductDescription("还行");
        p.setProductName("三星爆炸手机");
        p.setProductPrice(555.55);
        //index03中新增这个document数据，新增使用的mapping是默认的，其中分词器使用standard
        //product要转化为json对象
        IndexRequestBuilder request = client.prepareIndex("index03", "product", p.getProductId());
        //封装填写数据
        request.setSource(new ObjectMapper().writeValueAsString(p));

        // 请求/index03/product/id
        IndexResponse response = request.get();
        System.out.println(response.getVersion());

    }
    //获取文档对象
    @Test
    public void test03(){
        //获取文档数据的结构，代码中如何使用查询的结果
        GetResponse response = client.prepareGet("index03", "product", "product1").get();
        //map对象
        Map<String, Object> source = response.getSource();
        System.out.println(source.get("productName"));
        //json字符串
        String sourceStr = response.getSourceAsString();
        System.out.println(sourceStr);

    }
    //搜索 解析查询结果
    @Test
    public void test04(){
        //确定搜索的功能实现类 TermQuery mathQuery FuzzyQuery BooleanQuery
        TermQueryBuilder query = QueryBuilders.termQuery("productName", "三");
        //执行搜索，支持分页的 底层浅查询 先查到标识，然后根据定义的分页参数，筛选数据并获取真正的数据返回给你使用
        SearchRequestBuilder queryResult = client.prepareSearch("index03").setQuery(query);
        //相当于limit start
        queryResult.setFrom(0);
        //相当于limit start rows
        queryResult.setSize(5);
        SearchResponse searchResponse = queryResult.get();
        //解析数据 hits --> hits -->source
        SearchHits topHits = searchResponse.getHits();
        System.out.println("总条数：" + topHits.totalHits);
        System.out.println("最大评分：" + topHits.getMaxScore());
        SearchHit[] hits = topHits.getHits();
        for (SearchHit hit : hits) {
            //数据封装在Hit的source属性中
            hit.getIndex();
            hit.getVersion();
            hit.getType();
            Map<String, Object> map = hit.getSourceAsMap();
            System.out.println("map的数据：" + map.get("productName"));


        }
    }
}
