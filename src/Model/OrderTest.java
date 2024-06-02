package Model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import Model.Order;
import Model.FoodItem;

class OrderTest {
	
	private Order order;
	
	@BeforeEach
	public void setUp() {
		order = new Order();
	}

	@Test
	void testAddItem() {
		FoodItem burrito = new FoodItem("Burrito", 7.0, 1);
		order.addItem(burrito, 1);
		assertEquals(1, order.getItems().size());
		assertEquals(7.0, order.calculateTotal(), 0.001);
	}

	@Test
	void testCalculateTotal() {
		 FoodItem burrito = new FoodItem("Burrito", 7.0, 1);
	     FoodItem fries = new FoodItem("Fries", 4.0, 1);
	     order.addItem(burrito, 1);
	     order.addItem(fries, 1);
	     assertEquals(11.0, order.calculateTotal(), 0.001);// assert equal was found from youtube... do i have to credit this?
	     //https://www.youtube.com/watch?v=vZm0lHciFsQ&ab_channel=CodingwithJohn
	}

	@Test
	void testSetStatus() {
		LocalDateTime now = LocalDateTime.now();
        order.setOrderPlacedTime(now);
        assertEquals(now, order.getOrderPlacedTime());
	}

	@Test
	void testSetOrderPlacedTime() {
		order.setStatus("placed");
        assertEquals("placed", order.getStatus());
	}

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
