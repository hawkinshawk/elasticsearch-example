package com.mkyong;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.reindex.ReindexPlugin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.geo.CustomGeoModule;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

//http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-nosql.html#boot-features-connecting-to-elasticsearch-spring-data
//https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples/spring-boot-sample-data-elasticsearch/src/main/java/sample/data/elasticsearch
//http://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.repositories
//http://geekabyte.blogspot.my/2015/08/embedding-elasticsearch-in-spring.html
//https://github.com/spring-projects/spring-data-elasticsearch/wiki/Spring-Data-Elasticsearch---Spring-Boot---version-matrix
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.mkyong.book.repository")
public class EsConfig {

    @Value("${elasticsearch.host}")
    private String EsHost;

    @Value("${elasticsearch.port}")
    private int EsPort;

    @Value("${elasticsearch.clustername}")
    private String EsClusterName;

    @Bean
    public Client client() throws Exception {

        Settings esSettings = Settings.settingsBuilder()
                .put("cluster.name", EsClusterName)
                .put("script.engine.groovy.inline.update","on")
                .build();

        //https://www.elastic.co/guide/en/elasticsearch/guide/current/_transport_client_versus_node_client.html
        return TransportClient.builder()
                .settings(esSettings)
                .addPlugin(ReindexPlugin.class)
                .build()
                .addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName(EsHost), EsPort));
    }

    @Bean
    public EntityMapper entityMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.registerModule(new CustomGeoModule());
        objectMapper.registerModule(new JodaModule());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ");
        dateFormat.setTimeZone(TimeZone.getDefault());
        objectMapper.setDateFormat(dateFormat);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new ConfigurableEntityMapper(objectMapper);
    }

    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(Client client, EntityMapper entityMapper) {
        return new ElasticsearchTemplate(client, entityMapper);
    }

    /**
     * Class to configure object mapper
     */
    static class ConfigurableEntityMapper implements EntityMapper {

        private final ObjectMapper objectMapper;

        public ConfigurableEntityMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public String mapToString(Object object) throws IOException {
            String json = objectMapper.writeValueAsString(object);
            return json;
        }

        @Override
        public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
            return objectMapper.readValue(source, clazz);
        }
    }
}