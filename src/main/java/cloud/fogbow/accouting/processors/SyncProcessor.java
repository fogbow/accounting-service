package cloud.fogbow.accouting.processors;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cloud.fogbow.accouting.datastore.DatabaseManager;
import cloud.fogbow.accouting.models.*;
import cloud.fogbow.accouting.models.orders.OrderState;
import cloud.fogbow.accouting.models.specs.SpecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cloud.fogbow.accouting.models.orders.Order;


@Component
public class SyncProcessor implements Runnable {
	private static final int SLEEP_TIME = 60000; // One hour
	private static final Logger logger = LoggerFactory.getLogger(SyncProcessor.class);

	private AuditableOrderIdRecorder idRecorder;
	private SpecFactory specFactory = new SpecFactory();

	@Autowired
	private DatabaseManager dbManager;

	@Override
	public void run() {		
		Boolean isActive = true;
		while(isActive) {			
			logger.info("Updating tables.");
			try {
				checkIdRecorder();
				checkOrdersHistory();

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

	private void checkOrdersHistory() {
		List<AuditableOrderStateChange> auditableOrders = dbManager.getAllAuditableOrdersFromCurrentId(idRecorder.getCurrentId());

		for(AuditableOrderStateChange auditOrder : auditableOrders) {
			alignRecord(auditOrder);
		}

		int auditableOrdersSize = auditableOrders.size();

		if(auditableOrdersSize > 0) {
			idRecorder.setCurrentId(auditableOrders.get(auditableOrdersSize-1).getId());
			dbManager.saveIdRecorder(idRecorder);
		}
	}

	private void alignRecord(AuditableOrderStateChange auditOrder) {
		Record rec = dbManager.getRecordByOrderId(auditOrder.getOrder().getId());

		if(rec == null && auditOrder.getNewState().equals(OrderState.FULFILLED)) {
			createRecord(auditOrder);
		} else {
			if(orderHasFinished(auditOrder.getNewState())) {
				rec.setState(auditOrder.getNewState());
				rec.setEndTime(auditOrder.getTimestamp());
				setFinishedOrderDuration(auditOrder, rec);
				dbManager.saveRecord(rec);
			} else if(auditOrder.getNewState().equals(OrderState.UNABLE_TO_CHECK_STATUS)) {
				rec.setDuration(auditOrder.getTimestamp().getTime() - rec.getStartTime().getTime());
			} else if(auditOrder.getNewState().equals(OrderState.FULFILLED)) {
				rec.setDuration(0);
			}
		}
	}

	private void createRecord(AuditableOrderStateChange auditOrder) {
		Order ord = auditOrder.getOrder();

		Timestamp startTime = getStartTimestamp(auditOrder.getTimestamp());
		AccountingUser user = new AccountingUser(
			new UserIdentity(ord.getProvider(), ord.getUserId())
		);

		Record rec = new Record(
			ord.getId(),
			ord.getType().getValue(),
			specFactory.constructSpec(ord),
			ord.getRequester(),
			startTime,
			user
		);

		rec.setState(auditOrder.getNewState());

		dbManager.saveUser(user);
		dbManager.saveRecord(rec);
	}

	private void setFinishedOrderDuration(AuditableOrderStateChange auditOrder, Record rec) {
		if(orderHasFinished(auditOrder.getNewState()) && rec.getDuration() == 0) {
			rec.setDuration(rec.getEndTime().getTime() - rec.getStartTime().getTime());
		}
	}

	private Timestamp getStartTimestamp(Timestamp auditTimestamp) {
		Timestamp startTimestamp;

		try {
			Date auditDate = auditTimestamp;
			Date filteredDate = new SimpleDateFormat("yyyy-MM-dd").parse(auditDate.toString().split(" ")[0]);
			startTimestamp = new Timestamp(filteredDate.getTime());
		} catch (ParseException pe) {
			startTimestamp = auditTimestamp;
		}

		return startTimestamp;
	}

	private boolean orderHasFinished(OrderState state) {
		return state.equals((OrderState.CLOSED)) || state.equals(OrderState.FAILED_AFTER_SUCCESSUL_REQUEST);
	}

}
