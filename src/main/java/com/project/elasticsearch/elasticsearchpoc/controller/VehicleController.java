package com.project.elasticsearch.elasticsearchpoc.controller;

import com.project.elasticsearch.elasticsearchpoc.document.Vehicle;
import com.project.elasticsearch.elasticsearchpoc.search.SearchRequestDTO;
import com.project.elasticsearch.elasticsearchpoc.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        for(int i = Integer.parseInt(vehicle.getId()); i <= 500;i++) {
            Vehicle vehicle1 = new Vehicle();
            vehicle1.setId(i+"");
            vehicle1.setNumber(str+i);
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
}
