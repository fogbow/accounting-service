package accouting.api.http;

import accouting.constants.SystemConstants;
import accouting.services.RecordService;
import accouting.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ResourceUsageController.USAGE_ENDPOINT  )
public class ResourceUsageController {

    public static final String USAGE_ENDPOINT = "usage";

    @Autowired
    private RecordService recordService;

    @RequestMapping(value = "/{userId}/{requestingMember}/{providingMember}/{resourceType}/{initialDate}/{finalDate}", method = RequestMethod.GET)
    public ResponseEntity<List<Record>> getResourceUsageFromUser(
            @PathVariable String userId,
            @PathVariable String requestingMember,
            @PathVariable String providingMember,
            @PathVariable String resourceType,
            @PathVariable String initialDate,
            @PathVariable String finalDate,
            @RequestHeader(value = SystemConstants.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken
    ) throws Exception{
        return new ResponseEntity<List<Record>>(recordService.getUserRecords(
                userId, requestingMember, providingMember, resourceType, initialDate, finalDate, systemUserToken), HttpStatus.OK);
    }
}
