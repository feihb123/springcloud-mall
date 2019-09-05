package cn.datacharm.mallsearchservice.service;

import cn.datacharm.mallsearchservice.mapper.SearchMapper;
import cn.datacharm.pojo.Product;
import cn.datacharm.utils.MapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 *
 * @author Herb
 * @date 2019/09/2019-09-05
 */
@Service
public class SearchService {
    @Autowired
    private SearchMapper searchMapper;
    @Autowired
    private TransportClient client;

    public void createIndex() {
        //先判断索引是否存在
        IndicesAdminClient admin = client.admin().indices();
        IndicesExistsRequestBuilder request = admin.prepareExists("mall-index");
        IndicesExistsResponse response = request.get();
        if (!response.isExists()) {
            //说明不存在
            admin.prepareCreate("mall-index");
        }
        List<Product> products = searchMapper.queryAll();
        for (Product product : products) {
            //转换成json
            try {
                String pro = MapperUtil.MP.writeValueAsString(product);
                IndexRequestBuilder builder = client.prepareIndex("mall-index", "product", product.getProductId());
                IndexResponse indexResponse = builder.setSource(pro).get();



            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Product> query(String query, Integer page, Integer keyWord) {
        /**
         * 构建一个查询对象 获取分页的查询结果
         * hits中source是json数据
         * 转换成product对象 封装list返回
         */
        List<Product> products = new ArrayList<>();
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("productName",keyWord);
        SearchRequestBuilder request = client.prepareSearch("mall-index");

        return null;
    }

}
