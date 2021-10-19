package com.project.elasticsearch.elasticsearchpoc.controller;

import com.project.elasticsearch.elasticsearchpoc.document.Vehicle;
import com.project.elasticsearch.elasticsearchpoc.search.SearchRequestDTO;
import com.project.elasticsearch.elasticsearchpoc.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

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
        String str = "Vehicle ";
        Date date = new Date();
        for (int i = Integer.parseInt(vehicle.getId()); i <= 500; i++) {
            Vehicle vehicle1 = new Vehicle();
            vehicle1.setId(i + "");
            vehicle1.setNumber(str + i);
            vehicle1.setCreated(Date.from(date.toInstant().plus(i, ChronoUnit.DAYS)));
            vehicleService.index(vehicle1);
        }
    }

    @GetMapping("/{id}")
    public Vehicle getById(@PathVariable final String id) {
        return vehicleService.getById(id);
    }

    @PostMapping("/search")
    public List<Vehicle> search(@RequestBody final SearchRequestDTO dto) {
        return vehicleService.search(dto);
    }

    @GetMapping("/search/{date}")
    public List<Vehicle> getAllVehiclesCreatedSince(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") final Date date) {
       return vehicleService.getAllVehicleCreatedSince(date);
    }
    @PostMapping("/search/{from}/to/{to}")
    public List<Vehicle> getAllVehiclesCreatedSince(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") final Date from,
                                                    @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") final Date to) {
       return vehicleService.getAllVehicleCreatedSince(from, to);
    }
}
