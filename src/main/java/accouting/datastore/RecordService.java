package accouting.datastore;

import accouting.model.Record;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RecordService {

    @Autowired
    private DataBaseManager dbManager = DataBaseManager.getDbManager();

    public List<Record> getUserRecords(String userId) {
        return dbManager.getUserRecords(userId);
    }

    public void insertRecord(Record record) {
        dbManager.saveRecord(record);
    }
}
