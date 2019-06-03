package cloud.fogbow.accounting.api.http.request;

import cloud.fogbow.accounting.api.http.response.Record;
import cloud.fogbow.accounting.constants.Messages;
import cloud.fogbow.accounting.constants.SystemConstants;
import cloud.fogbow.accounting.core.ApplicationFacade;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ResourceUsage.USAGE_ENDPOINT)
public class ResourceUsage {
    public static final String USAGE_SUFFIX_ENDPOINT = "usage";
    public static final String USAGE_ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + USAGE_SUFFIX_ENDPOINT;

    private final Logger LOGGER = Logger.getLogger(ResourceUsage.class);

    @RequestMapping(value = "/{userId}/{requestingMember}/{providingMember}/{resourceType}/{initialDate}/{finalDate}",
                    method = RequestMethod.GET)
    public ResponseEntity<List<Record>> getResourceUsageFromOtherUser(
            @PathVariable String userId,
            @PathVariable String requestingMember,
            @PathVariable String providingMember,
            @PathVariable String resourceType,
            @PathVariable String initialDate,
            @PathVariable String finalDate,
            @RequestHeader(value = SystemConstants.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken
    ) throws Exception {
        try {
            List<Record> records = ApplicationFacade.getInstance().getUserRecords(userId, requestingMember, providingMember,
                    resourceType, initialDate, finalDate, systemUserToken);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info(String.format(Messages.Exception.GENERIC_EXCEPTION, e.getMessage()), e);
            throw e;
        }
    }

    @RequestMapping(value = "/{requestingMember}/{resourceType}/{initialDate}/{finalDate}", method = RequestMethod.GET)
    public ResponseEntity<List<Record>> getResourceUsageFromSystemUser(
            @PathVariable String requestingMember,
            @PathVariable String resourceType,
            @PathVariable String initialDate,
            @PathVariable String finalDate,
            @RequestHeader(value = SystemConstants.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken
    ) throws Exception {
        try {
            List<Record> records = ApplicationFacade.getInstance().getSelfRecords(requestingMember, resourceType, initialDate,
                finalDate, systemUserToken);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info(String.format(Messages.Exception.GENERIC_EXCEPTION, e.getMessage()), e);
            throw e;
        }
    }
}
