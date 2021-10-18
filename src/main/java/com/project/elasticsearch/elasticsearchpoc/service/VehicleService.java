package com.project.elasticsearch.elasticsearchpoc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.elasticsearch.elasticsearchpoc.document.Vehicle;
import com.project.elasticsearch.elasticsearchpoc.helper.Indices;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(VehicleService.class);

    private final RestHighLevelClient client;

    @Autowired
    public VehicleService(RestHighLevelClient client) {
        this.client = client;
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
