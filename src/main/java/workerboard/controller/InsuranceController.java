package workerboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import workerboard.model.InsuranceApplication;
import workerboard.serivce.InsuranceService;

@RestController
@CrossOrigin
@RequestMapping("/api/application")
public class InsuranceController {

    private InsuranceService insuranceService;

    @Autowired
    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @PostMapping
    ResponseEntity<InsuranceApplication> registerInsuranceApplication(@RequestBody InsuranceApplication insuranceApplication){
        return ResponseEntity.ok(insuranceService.registerInsuranceApplication(insuranceApplication));
    }
}