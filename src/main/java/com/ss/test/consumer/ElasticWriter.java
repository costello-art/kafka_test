package com.ss.test.consumer;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.List;

public class ElasticWriter {

    private RestHighLevelClient client;

    public ElasticWriter() {
        client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")));
    }

    public void write(String data) {
        IndexRequest indexRequest = new IndexRequest("ss_big_data", "doc");

        indexRequest.source(data, XContentType.JSON);

        try {
            client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeAll(List<String> bulkList) {
        if (bulkList.size() == 0) {
            return;
        }

        BulkRequest bulkRequest = new BulkRequest();

        bulkList.forEach(record -> {
            IndexRequest indexRequest = new IndexRequest("ss_big_data", "doc");
            indexRequest.source(record, XContentType.JSON);

            bulkRequest.add(indexRequest);
        });

        try {
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
