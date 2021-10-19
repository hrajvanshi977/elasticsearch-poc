package com.project.elasticsearch.elasticsearchpoc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.elasticsearch.elasticsearchpoc.document.Vehicle;
import com.project.elasticsearch.elasticsearchpoc.helper.Indices;
import com.project.elasticsearch.elasticsearchpoc.search.SearchRequestDTO;
import com.project.elasticsearch.elasticsearchpoc.search.util.SearchUtil;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class VehicleService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(VehicleService.class);

    private final RestHighLevelClient client;

    @Autowired
    public VehicleService(RestHighLevelClient client) {
        this.client = client;
    }

    public List<Vehicle> search(final SearchRequestDTO dto) {
        final SearchRequest request = SearchUtil.buildSearchRequest(Indices.VEHICLE_INDEX, dto);
        return searchInternal(request);
    }

    public List<Vehicle> getAllVehicleCreatedSince(final Date date) {
        final SearchRequest request = SearchUtil.buildSearchRequest(Indices.VEHICLE_INDEX, "created", date);
        return searchInternal(request);
    }
    public List<Vehicle> getAllVehicleCreatedSince(final Date from, final Date to) {
        final SearchRequest request = SearchUtil.buildSearchRequest(Indices.VEHICLE_INDEX, "created", from, to);
        return searchInternal(request);
    }

    private List<Vehicle> searchInternal(final SearchRequest request) {
        if (request == null) {
            LOG.error("Failed to build the search request");
            return Collections.emptyList();
        }
        try {
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            final SearchHit[] searchHits = response.getHits().getHits();
            final List<Vehicle> vehicles = new ArrayList<>(searchHits.length);
            for (SearchHit hit : searchHits) {
                vehicles.add(MAPPER.readValue(hit.getSourceAsString(), Vehicle.class));
            }
            return vehicles;
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public Boolean index(final Vehicle vehicle) {
        try {
            final String vehicleAsString = MAPPER.writeValueAsString(vehicle);
            final IndexRequest indexRequest = new IndexRequest(Indices.VEHICLE_INDEX);
            indexRequest.id(vehicle.getId());
            indexRequest.source(vehicleAsString, XContentType.JSON);
            final IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            return indexResponse != null && indexResponse.status().equals(RestStatus.OK);
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    public Vehicle getById(final String vehicleId) {
        try {
            final GetResponse documentFields = client.get(new GetRequest(Indices.VEHICLE_INDEX, vehicleId), RequestOptions.DEFAULT);
            if (documentFields == null || documentFields.isSourceEmpty()) {
                return null;
            }
            return MAPPER.readValue(documentFields.getSourceAsString(), Vehicle.class);
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }
}
