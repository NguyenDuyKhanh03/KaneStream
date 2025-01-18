package com.example.KaneStream.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchClient createElasticsearchClient() {

        // Cấu hình RestClient kết nối tới Elasticsearch
        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http"))
                .build();

        // Tạo một transport cho ElasticsearchClient sử dụng JacksonJsonpMapper
        RestClientTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );

        // Trả về ElasticsearchClient đã cấu hình
        return new ElasticsearchClient(transport);

    }
}
