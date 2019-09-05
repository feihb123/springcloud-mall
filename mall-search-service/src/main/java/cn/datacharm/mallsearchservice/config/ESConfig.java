package cn.datacharm.mallsearchservice.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * description:
 * 通过初始的属性读取生成框架维护的transportClient对象
 * @author Herb
 * @date 2019/09/2019-09-05
 */
@Configuration
@ConfigurationProperties(prefix = "es")
public class ESConfig {
    private List<String> nodes;

    @Bean
    public TransportClient initTransport(){
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
        // 解析nodes
        try {
            for (String node : nodes) {
                String host = node.split(":")[0];
                int port = Integer.parseInt(node.split(":")[1]);
                InetAddress es = InetAddress.getByName(host);
                //扩展功能中es给不同语言连接使用的底层接口9300
                TransportAddress address = new InetSocketTransportAddress(es, port);
                client.addTransportAddress(address);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
        return client;
    }
}
