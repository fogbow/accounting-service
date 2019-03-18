package accouting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import accouting.processors.SyncProcessor;

import javax.annotation.PostConstruct;

@Component
public class Sync {
	private static final Logger logger = LoggerFactory.getLogger(Sync.class);

	@Autowired
	SyncProcessor syncProcess;

	@PostConstruct
	public void tst() {
		new Thread(syncProcess).start();
	}
}
