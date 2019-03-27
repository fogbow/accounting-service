package accouting.processors;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	private static final int SLEEP_TIME = 60000; // One hour
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

	private void retriveAndSave() {
			List<AuditableOrderStateChange> auditableOrders = dbManager.getAllAuditableOrdersFromCurrentId(idRecorder.getCurrentId());
		System.out.println(idRecorder.getCurrentId());
		System.out.println("that wass the current id");
		for(AuditableOrderStateChange auditOrder : auditableOrders) {
			refreshRecord(auditOrder);
		}

		int auditableOrdersSize = auditableOrders.size();

		if(auditableOrdersSize > 0)
			idRecorder.setCurrentId(auditableOrders.get(auditableOrdersSize-1).getId());
		dbManager.saveIdRecorder(idRecorder);
	}

	private void refreshRecord(AuditableOrderStateChange auditOrder) {
		Record rec = dbManager.getRecordByOrderId(auditOrder.getOrder().getId());

		if(rec == null) {
			createRecord(auditOrder);
		} else {
			if(orderIsClosed(auditOrder.getNewState())) {
				rec.setState(auditOrder.getNewState());
				rec.setEndTime(auditOrder.getTimestamp());
				rec.setDuration(rec.getEndTime().getTime() - rec.getStartTime().getTime());
				dbManager.saveRecord(rec);
			}
		}
	}

	private void createRecord(AuditableOrderStateChange auditOrder) {
		Order ord = auditOrder.getOrder();

		Timestamp startTime = getFilteredTimestamp(auditOrder.getTimestamp());
		Record rec = new Record(
				ord.getId(),
				ord.getClass().getName(),
				"",
				"fogbow-mapper-test",
				"",
				ord.getRequester(),
				ord.getProvider(),
				startTime
		);

		rec.setState(auditOrder.getNewState());

		if(orderIsClosed(auditOrder.getNewState())) {
			rec.setDuration(rec.getEndTime().getTime() - rec.getStartTime().getTime());
		}

		dbManager.saveRecord(rec);
	}

	private Timestamp getFilteredTimestamp(Timestamp auditTimestamp) {
		Timestamp filteredTimestamp;

		try {
			Date tst = auditTimestamp;
			Date filteredDate = new SimpleDateFormat("yyyy-MM-dd").parse(tst.toString().split(" ")[0]);
			filteredTimestamp = new Timestamp(filteredDate.getTime());
		} catch (ParseException pe) {
			filteredTimestamp = auditTimestamp;
		}

		return filteredTimestamp;
	}

	private boolean orderIsClosed(OrderState state) {
		return state.equals((OrderState.CLOSED)) || state.equals(OrderState.DEACTIVATED)
				|| state.equals(OrderState.FAILED_AFTER_SUCCESSUL_REQUEST) || state.equals(OrderState.FAILED_ON_REQUEST);
	}

}
