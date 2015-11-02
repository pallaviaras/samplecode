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
		

		System.out.println(" Seat Map has reduced seat capacity to allow testing. Can be modified using level.data parameter in ticketService.config");
		System.out.println(" Best available seats are the closest to the stage ");
		System.out.println(" Hold expires in 8 second. Can be modified using hold.duration parameter in ticketService.config ");
		System.out.println(" Seats for which the hold expires, are made available for the next customer ");
		System.out.println(" Test takes about 1 minute to complete. This is to allow background processes(hold expiration) to complete.");
    	//System.out.println("@Before - classSetUp");
    }

    @AfterClass
    public static void classTearDown() {
    	dataservice.shutDown();
    	System.out.println("@After - classTearDown");
    }
	
    /*** 
     * 
     * Test simulate multithreaded behaviour. Customers put hold on tickets and wait certain specified time and then reserve them.
     * If customer waits longer that ticket hold period their hold expires. 
     * If customer confirms in hold period the customer gets a confirmation number.
     * 
     * 
     */
	@Test
	public void multiThreadedBooking(){
		//System.out.printlnNumber of seats("Start findHoldSeats Test");
		
		System.out.println("");
		System.out.println("Number of seats at level 1:  " +service.numSeatsAvailable(1));
		System.out.println("");
		
	
		Customer customer= new Customer("test1@yahoo.com", 6,7000);
		Thread thread1 = new Thread(customer);
		thread1.start();
		
		Customer customer2= new Customer("test2@yahoo.com", 2,4000);
		Thread thread2= new Thread(customer2);
		thread2.start();
		
		Customer customer3= new Customer("test3@yahoo.com", 4,5000);
		Thread thread3 = new Thread(customer3);
		thread3.start();
		
		Customer customer4= new Customer("test4@yahoo.com", 4,3000);
		Thread thread4 = new Thread(customer4);
		thread4.start();
		
		Customer customer5= new Customer("test5@yahoo.com", 4,6000);
		Thread thread5 = new Thread(customer5);
		thread5.start();
		
		
		
		
		// Put current thread in sleep to simulate to allow background processes to complete.
		try{
		Thread.sleep(80000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		

		System.out.println("Retrieve Booking for :  " + customer.getEmail() +  " and code " + customer.getConfirmCode()  + service.retreiveFinalBooking( customer.getConfirmCode()));
		System.out.println("Retrieve Booking for :  " + customer2.getEmail() +  " and code " + customer2.getConfirmCode() + service.retreiveFinalBooking(customer2.getConfirmCode()));
		System.out.println("Retrieve Booking for :  " + customer3.getEmail() +  " and code " + customer3.getConfirmCode() + service.retreiveFinalBooking(customer3.getConfirmCode()));
		System.out.println("Retrieve Booking for :  " + customer4.getEmail() +  " and code " + customer4.getConfirmCode() + service.retreiveFinalBooking(customer4.getConfirmCode()));
		System.out.println("Retrieve Booking for :  " + customer5.getEmail() +  " and code " + customer5.getConfirmCode() + service.retreiveFinalBooking(customer5.getConfirmCode()));
		System.out.println("Number of seats at level 1:  " +service.numSeatsAvailable(1));
		System.out.println("");
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
				//System.out.println(email +  " reviewing options "  +  sleepMilli/1000 + " sec");
				Thread.sleep(sleepMilli);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			SeatHold seatHold =  service.findAndHoldSeats(numSeats, minLevel, maxLevel ,email);
			System.out.println("Putting Hold for : " + seatHold);
			System.out.println("");
			try{
				System.out.println(email +  " waiting to confirm "  +  (sleepMilli +4000)/1000 + " sec");
				// More seconds the second time.
				Thread.sleep(sleepMilli+ 4000);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			String confirmString = service.reserveSeats(seatHold.getHoldId(), email);
			System.out.println("Reserve seats : " + email  +  "  "  + confirmString);
			System.out.println("");
			this.confirmCode= confirmString;
			
		}
	}
	
	
}