package com.project.elasticsearch.elasticsearchpoc.controller;

import com.project.elasticsearch.elasticsearchpoc.document.Vehicle;
import com.project.elasticsearch.elasticsearchpoc.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public void index(@RequestBody final Vehicle vehicle) {
        vehicleService.index(vehicle);
    }

    @GetMapping("/{id}")
    public Vehicle getById(@PathVariable final String id) {
        return vehicleService.getById(id);
    }
}
