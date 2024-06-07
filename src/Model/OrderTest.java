package Model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


class OrderTest {
	
	private Order order;
	
	//Did this to ensure that new order object is initialised 
	//before each test starts
	@BeforeEach
	public void setUp() {
		order = new Order();
	}

	//This test is to see the additem method works
	@Test
	void testAddItem() {
		FoodItem burrito = new FoodItem("Burrito", 7.0, 1);
		order.addItem(burrito, 1);
		assertEquals(1, order.getItems().size());
		assertEquals(7.0, order.calculateTotal(), 0.001);
	}

	//this test is to see if the calculatetotal method works
	@Test
	void testCalculateTotal() {
		FoodItem burrito = new FoodItem("Burrito", 7.0, 1);
	    FoodItem fries = new FoodItem("Fries", 4.0, 1);
	    order.addItem(burrito, 1);
	    order.addItem(fries, 1);
	    assertEquals(11.0, order.calculateTotal(), 0.001);
	}

	//this test is to test the set orderplacedtime method
	@Test
	void testSetOrderPlacedTime() {
		LocalDateTime now = LocalDateTime.now();
        order.setOrderPlacedTime(now);
        assertEquals(now, order.getOrderPlacedTime());
	}

	//tests the setStatus method 
	@Test
	void testSetStatus() {
		order.setStatus("placed");
        assertEquals("placed", order.getStatus());
	}

	//tests the tostring method
	@Test
	void testToString() {
		FoodItem burrito = new FoodItem("Burrito", 7.0, 1);
        order.addItem(burrito, 1);
        order.setStatus("placed");
        LocalDateTime now = LocalDateTime.now();
        order.setOrderPlacedTime(now);

        order.setTotalAmount(order.calculateTotal());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");// change now to flipppppppp
        String formattedPlacedTime = now.format(formatter);

        String expected = "Order ID: " + order.getOrderID() + "\n"
                        + "Status: placed\n"
                        + "Placed Time: " + formattedPlacedTime + "\n"
                        + "Collected Time: N/A\n"
                        + "Total Price: $7.00\n" 
                        + "Items: 1";
        
        assertEquals(expected, order.toString());
	}

}
