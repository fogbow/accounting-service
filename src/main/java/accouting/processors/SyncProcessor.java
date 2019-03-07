package accouting.processors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import accouting.datastore.DataBaseManager;
import accouting.datastore.OrderRepository;
import accouting.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import accouting.datastore.RecordRepository;
import accouting.model.Record;

public class SyncProcessor implements Runnable {
	private static final int SLEEP_TIME = 3600000; // One hour
	private static final Logger logger = LoggerFactory.getLogger(SyncProcessor.class);

//	@Autowired
//	private RecordRepository recordRepository;

	@Autowired
	private DataBaseManager dbManager = DataBaseManager.getDbManager();
	
	public SyncProcessor() {
	}

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
			extractRecord(ord);
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

	private void extractRecord(Order order) {
		System.out.println(order);
	}

}
