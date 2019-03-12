package accouting.http.api;

import accouting.datastore.RecordService;
import accouting.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping (value = "")
public class ResourceUsageController {

    public static final String USAGE_ENDPOINT = "usage";

    @Autowired
    private RecordService recordService;

    @RequestMapping(value = "/tst", method = RequestMethod.GET)
    public ResponseEntity<List<Record>> getResourceUsageFromUser(
            @PathVariable String userId,
            @PathVariable String requestingMember,
            @PathVariable String providingMember,
            @PathVariable String resourceType,
            @PathVariable String initialDate,
            @PathVariable String finalDate
    ) throws Exception{
        return new ResponseEntity<List<Record>>(recordService.getUserRecords(
                userId, requestingMember, providingMember, resourceType, initialDate, finalDate), HttpStatus.OK);
    }
}
