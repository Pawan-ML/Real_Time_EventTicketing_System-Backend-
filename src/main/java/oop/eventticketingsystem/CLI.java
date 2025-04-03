package oop.eventticketingsystem;

import oop.eventticketingsystem.model.configurations.Configuration;
import oop.eventticketingsystem.model.Model;
import oop.eventticketingsystem.model.users.Customer;
import oop.eventticketingsystem.model.users.Vendor;

import java.util.Scanner;
import java.util.concurrent.*;
import static java.lang.System.exit;


public class CLI {

    public static Scanner scanner;
    public static int totalTickets;
    public static int maximumTicketCapacity;
    public static int ticketsReleaseRate;
    public static int customerRetrievalRate;

    public static void getConfiguration() throws InterruptedException {
        while (true) {
            while (true) {
                scanner = new Scanner(System.in);
                System.out.print("Total number of Tickets: ");
                try {
                    totalTickets = scanner.nextInt();
                    if (totalTickets <= 0) {
                        System.out.println("Invalid Input! Please enter a positive integer and try again.");
                        continue;
                    }

                    break;
                } catch (Exception e) {
                    System.out.println("Invalid Input! Please enter a valid value and try again.");
                }

            }
            while (true) {
                scanner = new Scanner(System.in);
                System.out.print("Maximum Ticket Capacity: ");
                try {
                    maximumTicketCapacity = scanner.nextInt();
                    if (maximumTicketCapacity > totalTickets) {
                        System.out.println("Max Capacity cannot be greater than Total Tickets");
                        continue;
                    }
                    if (maximumTicketCapacity <= 0) {
                        System.out.println("Invalid Input! Ticket Capacity should be greater than 0. Please try again.");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid Input! Please enter a valid integer less than Total Tickets.");
                }
            }
            while (true) {
                scanner = new Scanner(System.in);
                System.out.print("Tickets Release Rate: ");
                try {
                    ticketsReleaseRate = scanner.nextInt();
                    if (ticketsReleaseRate > maximumTicketCapacity) {
                        System.out.println("Release Rate of Tickets cannot be greater than the Maximum Ticket Capacity. Please try again.");
                        continue;
                    }
                    if (ticketsReleaseRate <= 0) {
                        System.out.println("Invalid Input! Tickets Release Rate should be greater than 0. Please try again.");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid Input! Please enter a valid integer value.");
                }
            }
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Customer Retrieval Rate: ");
                try {
                    customerRetrievalRate = scanner.nextInt();
                    if (customerRetrievalRate >= maximumTicketCapacity) {
                        System.out.println("Retrieval Rate cannot be greater than the Maximum Ticket Capacity. Please try again.");
                        continue;
                    }
                    if (customerRetrievalRate <= 0) {
                        System.out.println("Invalid Input! Retrieval Rate should be greater than 0. Please try again.");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid Input! Please enter a valid integer value.");
                }
            }
            break;
        }

        Configuration systemConfigurations = new Configuration(totalTickets, ticketsReleaseRate, customerRetrievalRate, maximumTicketCapacity);
        Configuration.saveConfig(systemConfigurations);
        System.out.println("System configurations saved. Starting system...");
        Model.setConfiguration(systemConfigurations);

    }

    public static void startSystem(){
        Configuration systemConfigurations = Model.getConfiguration();

        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        ExecutorService vendorService = Executors.newFixedThreadPool(systemConfigurations.getTicketReleaseRate());
        ExecutorService customerService = Executors.newFixedThreadPool(systemConfigurations.getCustomerRetrievalRate());
        service.scheduleAtFixedRate(() -> {
            try {
                vendorService.execute(new Vendor());
            } catch (Exception e){
                Thread.currentThread().interrupt();
                vendorService.shutdown();
            }
        },0,4, TimeUnit.SECONDS);

        service.scheduleAtFixedRate(() -> {
            try {
                customerService.execute(new Customer());
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                customerService.shutdown();
            }
        },0,4, TimeUnit.SECONDS);


    }



    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome To The Event Ticketing System!");
        ExecutorService asyncThread = Executors.newFixedThreadPool(1);
        Future<?> futureTask;

        while (true){
            System.out.println("""
                1. Add new configurations and start system
                2. Load from existing system configurations and start system
                3. Add configurations.
                4. Exit\n""");
            System.out.print("Enter your choice(1-4): ");
            String option = scanner.nextLine();
            if (option.equals("1")) {
                getConfiguration();
                futureTask = asyncThread.submit(new Callable<>() {

                    @Override
                    public Object call() throws Exception {
                        System.out.println("Press Enter to stop the system. (Please wait while system handles incomplete processes to terminate, Thank you.)");
                        scanner.nextLine();
                        return null;
                    }
                });
//                Thread.sleep(2000);
                while (!futureTask.isDone()) {
                    startSystem();
                }
                System.out.println("Terminating running tasks...Please Wait");
//                Thread.currentThread().interrupt();
//                Runtime.getRuntime().exit(0);
                Runtime.getRuntime().addShutdownHook(new Thread(() ->{
                    Thread.currentThread().interrupt();
                    Runtime.getRuntime().exit(0);
                }));
                asyncThread.shutdownNow();
                exit(0);
                break;
            } else if (option.equals("2")) {
                futureTask = asyncThread.submit(() -> {
                    System.out.println("Press Enter to stop the system. (Please wait while system handles incomplete processes to terminate, Thank you.)");
                    scanner.nextLine();
                    return null;
                });
//                Thread.sleep(2000);
                while (!futureTask.isDone()) {
                    startSystem();
                }
                Thread.currentThread().interrupt();
                asyncThread.shutdown();
                Runtime.getRuntime().exit(0);
                exit(0);
                break;
            } else if (option.equals("3")) {
                getConfiguration();
            } else if (option.equals("4")) {
                System.out.println("Exiting system... Have a nice day!");
                exit(0);
            } else {
                System.out.println("Invalid Option! Enter an option between 1 - 4.\n");
            }
        }
    }
}