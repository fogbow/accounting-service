package cloud.fogbow.accs.core.models;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.junit.Test;

import cloud.fogbow.accs.core.models.orders.OrderState;

public class OrderStateHistoryTest {
	private static final Timestamp TIMESTAMP_1 = new Timestamp(1L);
	private static final Timestamp TIMESTAMP_2 = new Timestamp(2L);
	private OrderStateHistory stateHistory;
	
	@Test
	public void testUpdateAndGetHistory() {
		this.stateHistory = new OrderStateHistory();
		
		assertTrue(this.stateHistory.getHistory().isEmpty());
		
		this.stateHistory.updateState(OrderState.FULFILLED, TIMESTAMP_1);
		
		assertEquals(OrderState.FULFILLED, this.stateHistory.getHistory().get(TIMESTAMP_1));
		
		this.stateHistory.updateState(OrderState.HIBERNATED, TIMESTAMP_2);
		
		assertEquals(OrderState.FULFILLED, this.stateHistory.getHistory().get(TIMESTAMP_1));
		assertEquals(OrderState.HIBERNATED, this.stateHistory.getHistory().get(TIMESTAMP_2));
	}
}
