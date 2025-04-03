package oop.eventticketingsystem.service;

import oop.eventticketingsystem.model.Model;
import oop.eventticketingsystem.model.configurations.Configuration;
import oop.eventticketingsystem.repository.ConfigRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {


    @Autowired
    ConfigRepo configRepo;

    public void setSystemConfigurations(Configuration configuration) {
        Model.setConfiguration(configuration);
        Configuration.saveConfig(configuration);
        Configuration config = configRepo.findById(1).get();
        config.setCustomerRetrievalRate(configuration.getCustomerRetrievalRate());
        config.setTicketReleaseRate(configuration.getTicketReleaseRate());
        config.setNumberOfTickets(configuration.getNumberOfTickets());
        config.setMaxTicketCapacity(configuration.getMaxTicketCapacity());
        configRepo.save(config);// update configuration in the database
    }

}
