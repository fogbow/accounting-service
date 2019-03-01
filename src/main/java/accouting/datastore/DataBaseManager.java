package accouting.datastore;

import accouting.model.Record;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DataBaseManager {

    private static DataBaseManager dbManager;

    @Autowired
    private RecordRepository recordRepository;

    public DataBaseManager() {}

    public static DataBaseManager getDbManager() {
        if(dbManager == null) {
            dbManager = new DataBaseManager();
        }

        return dbManager;
    }

    public List<Record> getUserRecords(String userId) {
        return recordRepository.findByUserId(userId);
    }

    public void saveRecord(Record record) {
        recordRepository.save(record);
    }


}
