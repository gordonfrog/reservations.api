package com.drinkbird.restaurant.reservations.api;

import java.util.Arrays;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import com.drinkbird.restaurant.reservations.api.domain.CreateReservationData;
import com.drinkbird.restaurant.reservations.api.domain.DateId;
import com.drinkbird.restaurant.reservations.api.domain.ReservationCommand;
import com.drinkbird.restaurant.reservations.api.domain.ReservationCommandData;
import com.drinkbird.restaurant.reservations.api.domain.ReservationCommandProcessor;
import com.drinkbird.restaurant.reservations.api.domain.ReservationEventStore;
import com.drinkbird.restaurant.reservations.api.domain.ReservationException;
import com.drinkbird.restaurant.reservations.api.repositories.RawEventsRepository;

@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	
	@Bean
    CommandLineRunner init(RawEventsRepository domainRepository) {

        return args -> {
        	
	        	try {
		        	ReservationCommandData commandData =
		                    new CreateReservationData(
		                        "gordonfrog@gmail.com",
		                        "gordonfrog",
		                        5);
		        	ReservationCommand command = new ReservationCommand(new DateId(DateTime.now()), DateTime.now(), commandData);
		        	ReservationEventStore eventStore = new ReservationEventStore(domainRepository);
					ReservationCommandProcessor p = new ReservationCommandProcessor(eventStore);
					p.ProcessCommand(command, DateTime.now());
					
	        }
		    catch (ReservationException re) {
		    		if (re.getMessage().contains("already have a reservation")) {
		    			System.out.println("Reservation already exists: We are ready to go!");
		    		}
		    }
    };
  }
}
