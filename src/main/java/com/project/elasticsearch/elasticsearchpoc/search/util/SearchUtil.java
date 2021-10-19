package com.project.elasticsearch.elasticsearchpoc.search.util;

import com.project.elasticsearch.elasticsearchpoc.search.SearchRequestDTO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

public class SearchUtil {
    private SearchUtil() {
    }

    public static SearchRequest buildSearchRequest(final String indexName, final SearchRequestDTO dto) {
        try {
            final SearchSourceBuilder builder = new SearchSourceBuilder()
                    .postFilter(getQueryBuilder(dto));
            SearchRequest searchRequest = new SearchRequest(indexName);
            searchRequest.source(builder);
            return searchRequest;
        } catch (final Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static SearchRequest buildSearchRequest(
            final String indexName,
            final String field,
            final Date date) {
        try {
            final SearchSourceBuilder builder = new SearchSourceBuilder().postFilter(getQueryBuilder(field, date));
            final SearchRequest request = new SearchRequest(indexName);
            request.source(builder);
            return request;
        } catch (final Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static SearchRequest buildSearchRequest(
            final String indexName,
            final String field,
            final Date from,
            final Date to) {
        try {
            final SearchSourceBuilder builder = new SearchSourceBuilder().postFilter(getQueryBuilder(field, from, to)).size(100);
            final SearchRequest request = new SearchRequest(indexName);
            request.source(builder);
            return request;
        } catch (final Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static QueryBuilder getQueryBuilder(final String field, final Date date) {
        return QueryBuilders.rangeQuery(field).gte(date);
    }
    public static QueryBuilder getQueryBuilder(final String field, final Date from,  final Date to) {
        return QueryBuilders.rangeQuery(field).gte(from).lte(to);
    }


    public static QueryBuilder getQueryBuilder(final SearchRequestDTO searchRequestDTO) {
        if (searchRequestDTO == null) {
            return null;
        }

        final List<String> fields = searchRequestDTO.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }

        if (fields.size() > 1) {
            MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(searchRequestDTO.getSearchTerm())
                    .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
                    .operator(Operator.AND);
            fields.forEach(queryBuilder::field);
            return queryBuilder;
        }
        return fields.stream()
                .findFirst()
                .map(field -> QueryBuilders.matchQuery(field, searchRequestDTO.getSearchTerm())
                        .operator(Operator.AND))
                .orElse(null);
    }
}
