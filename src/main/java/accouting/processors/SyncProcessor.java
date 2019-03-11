package accouting.processors;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import accouting.datastore.DataBaseManager;
import accouting.datastore.OrderRepository;
import accouting.model.Order;
import accouting.model.OrderState;
import accouting.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Component
public class SyncProcessor implements Runnable {
	private static final int SLEEP_TIME = 3600000; // One hour
	private static final Logger logger = LoggerFactory.getLogger(SyncProcessor.class);

//	@Autowired
//	private RecordRepository recordRepository;

	@Autowired
	private DataBaseManager dbManager;

	@Autowired
    private OrderRepository orderRepository;

	public SyncProcessor() {
	}

	@Override
    @PostConstruct
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
			Record rec = extractRecord(ord);

			if(ord.getOrderState().equals(OrderState.CLOSED)) {
			    rec.setDuration(0);
            }

			dbManager.saveRecord(rec);
		}

	}

	
//	private void retriveAndSave() {
//		this.jdbcTemplate.query(
//				"SELECT t.* FROM timestamp as t INNER JOIN order_table as o on t.order_id=o.id WHERE (t.order_state=? OR t.order_state=?)",
//
//				new Object[] { "FULFILLED", "CLOSED" },
//				(rs, rowNum) -> extractRecord(rs)
//				).forEach(record -> this.recordRepository.save(record));
//	}
//
//	private Record extractRecord(ResultSet rs) throws SQLException {
//		return new Record(rs.getString("orderId"), rs.getString("resourceType"),
//				rs.getString("spec"), rs.getString("userId"), rs.getString("userName"),
//				rs.getString("requestingMember"), rs.getString("providingMember"), rs.getLong("startTime"));
//	}

	private Record extractRecord(Order order) {
		return new Record(
		        order.getId(), order.getClass().getName(), "", order.getInstanceId(), "", order.getRequester(), order.getProvider(), new Timestamp(System.currentTimeMillis())
        );
	}

}
