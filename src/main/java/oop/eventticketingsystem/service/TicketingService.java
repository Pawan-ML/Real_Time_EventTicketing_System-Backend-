package oop.eventticketingsystem.service;

import oop.eventticketingsystem.model.Model;
import oop.eventticketingsystem.model.configurations.Configuration;
import oop.eventticketingsystem.model.users.Customer;
import oop.eventticketingsystem.model.users.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TicketingService {


    @Autowired
    private Vendor vendor;
    @Autowired
    private Customer customer;

    public static Configuration systemConfigurations = Model.getConfiguration();

    public ScheduledExecutorService service;// = Executors.newScheduledThreadPool(2);
    public ExecutorService vendorService;// = Executors.newFixedThreadPool(systemConfigurations.getTicketReleaseRate());
    public ExecutorService customerService;// = Executors.newFixedThreadPool(systemConfigurations.getCustomerRetrievalRate());

    public void startTicketingService(){
        service = Executors.newScheduledThreadPool(2);
        vendorService = Executors.newFixedThreadPool(systemConfigurations.getTicketReleaseRate());
        customerService = Executors.newFixedThreadPool(systemConfigurations.getCustomerRetrievalRate());
        service.scheduleAtFixedRate(() -> {
            try {
                vendorService.submit(vendor);
            } catch (Exception e){
                Thread.currentThread().interrupt();
                vendorService.shutdown();
            }
        },0,4, TimeUnit.SECONDS);

        service.scheduleAtFixedRate(() -> {
            try {
                customerService.submit(customer);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                customerService.shutdown();
            }
        },0,3, TimeUnit.SECONDS);
    }

    public void stopTicketingService() throws InterruptedException {
        vendorService.shutdownNow();
        customerService.shutdownNow();
        service.shutdownNow();
        Thread.currentThread().interrupt();
//        Thread.getAllStackTraces().keySet().forEach(System.out::println);
    }
}
