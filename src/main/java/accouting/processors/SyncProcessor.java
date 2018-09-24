package accouting.processors;

import javax.sql.DataSource;

import org.postgresql.Driver;
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
	
	private JdbcTemplate jdbcTemplate;
	
	private String jdbcUrl = "jdbc:postgresql://localhost:5432/accounting";
	
	private String username = "accounting";
	
	private String password = "accounting";

	@Autowired
	private RecordRepository recordRepository;
	
	public SyncProcessor() {
	    Driver driver = new Driver();
	    DataSource dataSource = new SimpleDriverDataSource(driver, jdbcUrl, username, password);
	    this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void run() {		
		Boolean isActive = true;
		while(isActive) {			
			logger.info("Updating tables.");
			try {
				this.jdbcTemplate.query(
						"SELECT * FROM timestamp WHERE order_state=?  OR order_state=?",
						
						new Object[] { "FULFILLED", "CLOSED" },
						(rs, rowNum) -> new Record(rs.getString("orderId"), rs.getString("resourceType"),
								rs.getString("spec"), rs.getString("userId"), rs.getString("userName"),
								rs.getString("requestingMember"), rs.getString("providingMember"), rs.getLong("startTime"))
						
						).forEach(record -> this.recordRepository.save(record));
				
				logger.info("Finish updating.");
				
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				logger.info("Problem on updating records.");
				isActive = false;
			}
		}

	}

}
