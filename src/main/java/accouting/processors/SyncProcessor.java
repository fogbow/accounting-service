package accouting.processors;

import java.sql.Timestamp;
import java.util.List;

import accouting.datastore.DatabaseManager;
import accouting.model.Order;
import accouting.model.OrderState;
import accouting.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SyncProcessor implements Runnable {
	private static final int SLEEP_TIME = 600000; // One hour
	private static final Logger logger = LoggerFactory.getLogger(SyncProcessor.class);

	@Autowired
	private DatabaseManager dbManager;

	@Override
	public void run() {		
		Boolean isActive = true;
		while(isActive) {			
			logger.info("Updating tables.");
			try {
				this.retriveAndSave();
				
				logger.info("Finish updating.");
				
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				logger.info("Problem on updating records.");
				isActive = false;
			}
		}
	}

	private void retriveAndSave() {
		List<Order> orders = this.dbManager.getAllOrders();

		for(Order ord : orders) {
			if(!dbManager.existsRecordByOrderId(ord.getId())) {
				Record rec = extractRecord(ord);

				if(ord.getOrderState().equals(OrderState.CLOSED)) {
					rec.setDuration(0);
				}

				dbManager.saveRecord(rec);
			}
		}

	}

	private Record extractRecord(Order order) {
		return new Record(
		        order.getId(), order.getClass().getName(), "", "fogbow-mapper-test", "", order.getRequester(), order.getProvider(), new Timestamp(System.currentTimeMillis())
        );
	}

}
