package accouting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import accouting.processors.SyncProcessor;

@Component
public class Sync implements ApplicationRunner {
	private static final Logger logger = LoggerFactory.getLogger(Sync.class);

	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info("Starting thread to update records.");
		
		Thread syncProcess = new Thread(new SyncProcessor(), "sync-records");
		syncProcess.start();
	}
}
