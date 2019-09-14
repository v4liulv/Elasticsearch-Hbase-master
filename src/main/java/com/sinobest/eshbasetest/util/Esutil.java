package com.sinobest.eshbasetest.util;

import com.alibaba.fastjson.JSONObject;
import com.sinobest.eshbasetest.domain.Doc;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Esutil {
    private static final Logger LOG = LoggerFactory.getLogger(Esutil.class);
    private static RestHighLevelClient client;//获取一个ES的客户端对象叫client
    private static final String CLUSTER_NAME = "data-lakes";
    private static final String HOST_IP = "hadoop01";
    private static final int TCP_PORT = 9300;
    private static final int HTTP_PORT = 9200;

    static {
        client = getClientByHighLevel();
    }

    /**
     * 获取高级=客户端
     *
     * @return RestHighLevelClient
     */
    private static RestHighLevelClient getClientByHighLevel() {
        if (client == null) {
            synchronized (RestHighLevelClient.class) {
                client = new RestHighLevelClient(RestClient.builder(new HttpHost(HOST_IP, HTTP_PORT, "http")));
            }
        }
        return client;
    }

    public static void addIndex(String index, String type, Doc doc) {
        //Json字符串方式创建IndexRequest
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id(String.valueOf(doc.getId()));
        String json = JSONObject.toJSONString(doc);
        indexRequest.source(json, XContentType.JSON);

        IndexResponse indexResponse = null;
        try {
            indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert indexResponse != null;
        indexResponse.getId();
    }

    public static Map<String, Object> search(String key, String index, String type, int start, int row) {
        LOG.info("ES查询参数[index:{}, type:{}, start:{}, row: {}, queryValue: {}]", index, type, start, row, key);

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.field("describe");
        highlightBuilder.field("author");
        highlightBuilder.preTags("<font color='red' >");
        highlightBuilder.postTags("</font>");

        SearchResponse searchResponse = null;
        if (StringUtils.isNotBlank(key)) {
            SearchSourceBuilder searchRequestBuilder = new SearchSourceBuilder();
            searchRequestBuilder.highlighter(highlightBuilder).from(start).size(row);
            searchRequestBuilder.query(QueryBuilders.multiMatchQuery(key, "title", "describe", "author"));

            SearchRequest searchRequest = new SearchRequest(index).searchType(SearchType.DFS_QUERY_THEN_FETCH).source(searchRequestBuilder);
            try {
                searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(searchResponse == null ) return null;
        SearchHits hits = searchResponse.getHits();
        long total = hits.getTotalHits().value;
        LOG.info("查询总数:{}", total);
        Map<String, Object> map = new HashMap<>();
        SearchHit[] hits2 = hits.getHits();
        map.put("count", total);
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit searchHit : hits2) {
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            HighlightField highlightField = highlightFields.get("title");
            Map<String, Object> source = searchHit.getSourceAsMap();
            if (highlightField != null) {
                Text[] fragments = highlightField.fragments();
                StringBuilder name = new StringBuilder();
                for (Text text : fragments) {
                    name.append(text);
                }
                source.put("title", name.toString());
            }
            HighlightField highlightField2 = highlightFields.get("describe");
            if (highlightField2 != null) {
                Text[] fragments = highlightField2.fragments();
                StringBuilder describe = new StringBuilder();
                for (Text text : fragments) {
                    describe.append(text);
                }
                source.put("describe", describe.toString());
            }
            HighlightField highlightField3 = highlightFields.get("author");
            if (highlightField3 != null) {
                Text[] fragments = highlightField3.fragments();
                StringBuilder describe = new StringBuilder();
                for (Text text : fragments) {
                    describe.append(text);
                }
                source.put("author", describe.toString());
            }

            source.put("id", searchHit.getId());

            list.add(source);
        }
        map.put("dataList", list);
        return map;
    }
}
