package cloud.fogbow.accouting.processors;

import java.sql.Timestamp;
import java.text.DateFormat;
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

	@Autowired
	private SpecFactory specFactory;

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
			idRecorder.setCurrentId(auditableOrders.get(auditableOrdersSize-1).getId()+1);
			dbManager.saveIdRecorder(idRecorder);
		}
	}

	private void alignRecord(AuditableOrderStateChange auditOrder) {
		Record rec = dbManager.getRecordByOrderId(auditOrder.getOrder().getId());

		if(rec == null) {
			createRecord(auditOrder);
		} else {
			if(orderHasFinished(auditOrder.getNewState())) {
				rec.setState(auditOrder.getNewState());
				rec.setEndTime(auditOrder.getTimestamp());
				rec.setEndDate(extractDateFromTimestamp(auditOrder.getTimestamp()));
				setClosedOrderDuration(auditOrder, rec);
			} else if(auditOrder.getNewState().equals(OrderState.UNABLE_TO_CHECK_STATUS)) {
				rec.setDuration(getDuration(auditOrder.getTimestamp(), rec.getStartTime()));
				rec.setState(auditOrder.getNewState());
			} else if(auditOrder.getNewState().equals(OrderState.FULFILLED)) {
				rec.setState(auditOrder.getNewState());
				rec.setDuration(0);
				if(rec.getStartTime() == null) {
					rec.setStartTime(auditOrder.getTimestamp());
				}

			}
			dbManager.saveRecord(rec);
		}
	}

	private void createRecord(AuditableOrderStateChange auditOrder) {
		Order ord = auditOrder.getOrder();

		AccountingUser user = new AccountingUser(
			new UserIdentity(ord.getProvider(), ord.getUserId())
		);

		Record rec = new Record(
			ord.getId(),
			ord.getType().getValue(),
			specFactory.constructSpec(ord),
			ord.getRequester(),
			user
		);

		rec.setStartDate(extractDateFromTimestamp(auditOrder.getTimestamp()));

		AuditableOrderStateChange auditOrderToFulfilledState = dbManager.getFulfilledStateChange(ord.getId());
		if(auditOrderToFulfilledState != null && auditOrderToFulfilledState.getTimestamp().getTime() < auditOrder.getTimestamp().getTime()) {
			if(orderHasFinished(auditOrder.getNewState())) {
				rec.setStartTime(auditOrderToFulfilledState.getTimestamp());
				rec.setEndTime(auditOrder.getTimestamp());
				rec.setEndDate(extractDateFromTimestamp(auditOrder.getTimestamp()));
				setClosedOrderDuration(auditOrder, rec);
			} else if(!auditOrder.getNewState().equals(OrderState.FULFILLED)) {
				rec.setStartTime(auditOrderToFulfilledState.getTimestamp());
				rec.setDuration(auditOrder.getTimestamp().getTime() - rec.getStartTime().getTime());
			}
		}

		if(auditOrder.getNewState().equals(OrderState.FULFILLED)) {
			rec.setStartTime(auditOrder.getTimestamp());
			rec.setDuration(0);
		}

		rec.setState(auditOrder.getNewState());

		dbManager.saveUser(user);
		dbManager.saveRecord(rec);
	}

	private void setClosedOrderDuration(AuditableOrderStateChange auditOrder, Record rec) {
		if(orderHasFinished(auditOrder.getNewState()) && rec.getDuration() == 0) {
			rec.setDuration(getDuration(rec.getEndTime(), rec.getStartTime()));
		}
	}

	private long getDuration(Timestamp intervalEnd, Timestamp intervalStart) {
		if(intervalEnd != null && intervalStart != null) {
			return intervalEnd.getTime() - intervalStart.getTime();
		}

		return 0;
	}

	private boolean orderHasFinished(OrderState state) {
		return state.equals((OrderState.CLOSED)) || state.equals(OrderState.FAILED_AFTER_SUCCESSFUL_REQUEST) || state.equals((OrderState.DEACTIVATED));
	}

	private Timestamp extractDateFromTimestamp(Timestamp timestamp) {
		try {
			DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			Date d = f.parse(f.format((Date) timestamp));
			return new Timestamp(d.getTime());
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return null;
	}

}
