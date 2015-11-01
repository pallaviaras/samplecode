package com.ticketservice.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ticketservice.interfaces.TicketService;


public class TicketServiceApplication {

	
	public static void main(String[] args){
		ApplicationContext context = new ClassPathXmlApplicationContext( new String[] {
				"ticketServiceBean.xml"
			} );
		TicketService service =(TicketService)context.getBean("ticketService");
		System.out.println(service.numSeatsAvailable(1));
		
		
		System.out.println("Seat Hold1: " + service.findAndHoldSeats(6, 1, 3, "pallavi_aras@hotmail.con"));
		try{
			Thread.sleep(3000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		System.out.println("Seat Hold2: " + service.findAndHoldSeats(2, 2, 4, "pallavi_aras123@hotmail.con"));
		try{
			Thread.sleep(3000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		System.out.println("Seat Hold3: " + service.findAndHoldSeats(4, 1, 4, "pallavi_aras123@hotmail.con"));
	}
}
