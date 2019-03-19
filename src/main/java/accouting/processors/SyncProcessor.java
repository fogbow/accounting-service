package accouting.processors;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import accouting.datastore.DatabaseManager;
import accouting.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SyncProcessor implements Runnable {
	private static final int SLEEP_TIME = 600000; // One hour
	private static final Logger logger = LoggerFactory.getLogger(SyncProcessor.class);

	private AuditableOrderIdRecorder idRecorder;

	@Autowired
	private DatabaseManager dbManager;

	@Override
	public void run() {		
		Boolean isActive = true;
		while(isActive) {			
			logger.info("Updating tables.");
			try {
				checkIdRecorder();
				this.retriveAndSave();
				
				logger.info("Finish updating.");
				
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				logger.info("Problem on updating records.");
				isActive = false;
			}
		}
	}

	private void checkIdRecorder() {
		if(idRecorder == null) {
			idRecorder = dbManager.getIdRecorder();
		}
	}

//	private void retriveAndSave() {
//		List<Order> orders = this.dbManager.getAllOrders();
//
//		for(Order ord : orders) {
//			if(!dbManager.existsRecordByOrderId(ord.getId())) {
//				Record rec = extractRecord(ord);
//
//				if(ord.getOrderState().equals(OrderState.CLOSED)) {
//					rec.setDuration(0);
//				}
//
//				dbManager.saveRecord(rec);
//			}
//		}
//
//	}

	private void retriveAndSave() {
		List<AuditableOrderStateChange> auditableOrders = dbManager.getAllAuditableOrdersFromCurrentId(idRecorder.getCurrentId());

		for(AuditableOrderStateChange auditOrder : auditableOrders) {
			refreshRecord(auditOrder);
		}

		updateRecords();
	}

	private void updateRecords() {
		List<Record> records = dbManager.getFullFilledRecords();

		for(Record rec : records) {
			long now = new Date().getTime();
			long currentDuration = rec.getDuration();

			if(currentDuration > 0) {
				rec.setDuration(currentDuration + (now - currentDuration));
			} else {
				rec.setDuration(now - rec.getStartTime().getTime());
			}
		}
	}

	private void refreshRecord(AuditableOrderStateChange auditOrder) {
		Record rec = dbManager.getRecordByOrderId(auditOrder.getOrder().getId());

		if(rec == null) {
			if(auditOrder.getNewState().equals(OrderState.FULFILLED)) {
				createRecord(auditOrder);
			}
		} else {
			if(orderIsClosed(auditOrder.getNewState())) {
				rec.setState(auditOrder.getNewState());
			}
		}
	}

	private void createRecord(AuditableOrderStateChange auditOrder) {
		Order ord = auditOrder.getOrder();
		Record rec = new Record(
				ord.getId(),
				ord.getClass().getName(),
				"",
				"fogbow-mapper-test",
				"",
				ord.getRequester(),
				ord.getProvider(),
				auditOrder.getTimestamp()
		);
		long now = new Date().getTime();
		rec.setDuration(now - rec.getStartTime().getTime());
		dbManager.saveRecord(rec);
	}

	private boolean orderIsClosed(OrderState state) {
		return state.equals((OrderState.CLOSED)) || state.equals(OrderState.DEACTIVATED)
				|| state.equals(OrderState.FAILED_AFTER_SUCCESSUL_REQUEST) || state.equals(OrderState.FAILED_ON_REQUEST);
	}

}
