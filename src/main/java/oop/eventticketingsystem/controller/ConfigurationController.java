package oop.eventticketingsystem.controller;

import oop.eventticketingsystem.model.configurations.Configuration;
import oop.eventticketingsystem.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/systemConfigurations")
public class ConfigurationController {

    @Autowired
    public ConfigurationService configService;

    @PostMapping("/addConfigurations")
    public ResponseEntity<String> getConfiguration(@RequestBody Configuration config) {
        configService.setSystemConfigurations(config);
        return ResponseEntity.ok("Configuration successfully added.");
    }

//    @GetMapping("getConfig/{id}")
//    public ResponseEntity<Configuration> getConfigurationById(@PathVariable int id) {
//        Configuration config = configService.getSystemConfiguration(id);
//        return ResponseEntity.ok(config);
//    }

}
