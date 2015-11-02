Ticket Service Application

Assumptions: Best Available seats are considered to be seats closest to the stage. So if a level is specified, the search begins in the lowest row Number for avaialable seats
e.g Level 1 specified, search starts at row 1, If Level 2 specified search starts row 1 of Level 2, If row 1 has no seats available then move to row 2 etc.


PreReq:
Build tool used is Ant. Please ensure ant 1.7 is in PATH.
Java version used in 1.6

Steps:

Download code 
Setup Java and Ant.
command line invocation that will compile the java classes, the Junit tests and execute a multithreaded test.
execute at command line :  ant  run-ticketservice-tests


Explanation:

Seat class, has a synchronized method "selectSeatIfAvailable" , that will check the seat's availablity and sets itself as unavailable.

Seat Hold class object, contains a list of seats that are put on hold. Along with email address of customer and hold Id. It also has a unblock method that will set all the seats it contains as available.

TicketService interface is implemented by TicketServiceimpl class. TicketServiceImpl then calls  underlying data class named Dataservice.

Dataservice class is called to perform the main operation on all the data objects.It holds in memory map called Seat Map.
Seat Map is created using a configurable parameter to create levels, rows and seats and store in the Map.

findHold operation: 
This method searches from a specified level. The best seats are the ones closest to the stage so the search  always begins in row1 of each level. If seats are available then they are held else the search continues to next row or level. When required number of seats are held, the seathold object is created. 
We also put the seathold object in a future runnable which calls SeatHold's unblock method. Te unblock method,  makes the seats available again. It will expire the hold in a specified amount of time.

Reserve seats:
This method takes a holdId and find the future unblock task and cancels it. This will confirm the seats.It returns the confirmation Id.

Dependency injection and properties, specified by using ticketServiceBean.xml and ticketService.config file

 
Junit tests
A single multiThreadedBooking test is implemented and it creates 5 threads representing 5 customers putting orders for holds and confirming reserations. Various sleep times are assigned to the customers to allow few customer bookings to go through and certain to expire.
The results of the execution, are in the test-results directory. The results display messages related to various action like  putting seats  on hold,removal of seats from hold, reservation of seats etc.

Note: Test runs for a minute before displaying results. This is to simulate an operation that allows background processes to complete. 


Note: If seats start coming out of Hold then the next customer will start getting allocated those seats. Seats allocated to a customer might not be contigious. That would be an enhancemet to this algorithm

 
