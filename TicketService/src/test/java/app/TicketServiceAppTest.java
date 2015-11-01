package app; 

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ticketservice.bo.SeatHold;
import com.ticketservice.interfaces.TicketService;
import com.ticketservice.services.DataService;


@ContextConfiguration(locations = "/ticketServiceBean.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TicketServiceAppTest{
	
	static TicketService service =null;
	static DataService dataservice =null;
	@BeforeClass
    public  static void classSetUp() {
		ApplicationContext context = new ClassPathXmlApplicationContext( new String[] {
				"ticketServiceBean.xml"
			} );
		service =(TicketService)context.getBean("ticketService");
		dataservice =(DataService)context.getBean("dataService");
    	//System.out.println("@Before - classSetUp");
    }

    @AfterClass
    public static void classTearDown() {
    	dataservice.shutDown();
    	System.out.println("@After - classTearDown");
    }
	

	public void findHoldSeats(){
		//System.out.printlnNumber of seats("Start findHoldSeats Test");
		System.out.println("Number of seats at level 1:  " +service.numSeatsAvailable(1));
		SeatHold seatHold1 =  service.findAndHoldSeats(6, 1, 3, "test1@yaho.com");
		System.out.println("Putting Hold for : " + seatHold1);
		System.out.println("");
		try{
			Thread.sleep(15000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		String confirmString = service.reserveSeats(seatHold1.getHoldId(), "test1@yaho.com");
		System.out.println("Confirming for : " + seatHold1.getHoldId()  + " Confirmation: "  + confirmString);
		
		
		System.out.println("Putting Hold  for: " + service.findAndHoldSeats(2, 1, 4, "test2@yaho.com"));
		System.out.println("");
		try{
			Thread.sleep(20000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		System.out.println("Putting Hold for " + service.findAndHoldSeats(4, 1, 4, "test3@yaho.com"));
		System.out.println("");
		try{
			Thread.sleep(30000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		System.out.println("Number of seats at level 1:  " +service.numSeatsAvailable(1));
		dataservice.finalState();
		dataservice.retreiveFinalBooking(confirmString);
		//System.out.println("End findHoldSeats Test");
	}
	@Test
	public void multiThreadedBooking(){
		//System.out.printlnNumber of seats("Start findHoldSeats Test");
		System.out.println("Number of seats at level 1:  " +service.numSeatsAvailable(1));
		System.out.println("");
		
	
		Customer customer= new Customer("test1@yahoo.com", 6,10000);
		Thread thread1 = new Thread(customer);
		thread1.start();
		
		Customer customer2= new Customer("test2@yahoo.com", 2,7000);
		Thread thread2= new Thread(customer2);
		thread2.start();
		
		Customer customer3= new Customer("test3@yahoo.com", 4,12000);
		Thread thread3 = new Thread(customer3);
		thread3.start();
		
		Customer customer4= new Customer("test4@yahoo.com", 4,8000);
		Thread thread4 = new Thread(customer4);
		thread4.start();
		
		Customer customer5= new Customer("test5@yahoo.com", 4,10000);
		Thread thread5 = new Thread(customer5);
		thread5.start();
		
		
		System.out.println("Number of seats at level 1:  " +service.numSeatsAvailable(1));
		System.out.println("");
		
		// Put current thread in sleep to simulate to allow background processes to complete.
		try{
		Thread.sleep(50000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		

		System.out.println("Retrieve Booking for :  " + customer.getEmail()  + service.retreiveFinalBooking( customer.getConfirmCode()));
		System.out.println("Retrieve Booking for :  " + customer2.getEmail() + service.retreiveFinalBooking(customer2.getConfirmCode()));
		System.out.println("Retrieve Booking for :  " + customer3.getEmail() + service.retreiveFinalBooking(customer3.getConfirmCode()));
		System.out.println("Retrieve Booking for :  " + customer4.getEmail() + service.retreiveFinalBooking(customer4.getConfirmCode()));
		System.out.println("Retrieve Booking for :  " + customer5.getEmail() + service.retreiveFinalBooking(customer5.getConfirmCode()));
		//System.out.println("End findHoldSeats Test");
	}
	
	private class Customer implements Runnable{
		private String email;
		private int numSeats;
		private int minLevel=1;
		private int maxLevel =4;
		private long sleepMilli=30000;
		private String confirmCode;
		
		public String getConfirmCode() {
			return confirmCode;
		}
		
		public String getEmail() {
			return email;
		}

		public Customer (String email, int numSeats, long sleepMilli){
			this.email = email;
			this.numSeats =numSeats;
			this.sleepMilli= sleepMilli;
		}
		public void run (){
			try{
				System.out.println(email +  " reviewing options "  +  sleepMilli/1000 + " sec");
				Thread.sleep(sleepMilli);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			SeatHold seatHold =  service.findAndHoldSeats(numSeats, minLevel, maxLevel ,email);
			System.out.println("Putting Hold for : " + seatHold);
			System.out.println("");
			try{
				System.out.println(email +  " waiting to confirm "  +  sleepMilli/1000 + " sec");
				Thread.sleep(sleepMilli);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			String confirmString = service.reserveSeats(seatHold.getHoldId(), email);
			System.out.println("Reserve seats : " + email  +  confirmString);
			System.out.println("");
			this.confirmCode= confirmString;
			
		}
	}
	
	
}