package cloud.fogbow.accs.api.http.request;

import cloud.fogbow.accs.api.http.response.Record;
import cloud.fogbow.accs.constants.Messages;
import cloud.fogbow.accs.constants.SystemConstants;
import cloud.fogbow.accs.core.ApplicationFacade;
import cloud.fogbow.accs.constants.ApiDocumentation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ResourceUsage.USAGE_ENDPOINT)
@Api(description = ApiDocumentation.ResourceUsage.API)
public class ResourceUsage {
    public static final String USAGE_SUFFIX_ENDPOINT = "usage";
    public static final String USAGE_ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + USAGE_SUFFIX_ENDPOINT;

    private final Logger LOGGER = Logger.getLogger(ResourceUsage.class);

    @ApiOperation(value = ApiDocumentation.ResourceUsage.GET_OPERATION_FROM_OTHER_USER)
    @RequestMapping(value = "/{userId}/{requester}/{provider}/{resourceType}/{initialDate}/{finalDate}",
                    method = RequestMethod.GET)
    public ResponseEntity<List<Record>> getResourceUsageFromOtherUser(
    		@ApiParam(value = ApiDocumentation.ResourceUsage.USER_ID)
    		@PathVariable String userId,
            @ApiParam(value = ApiDocumentation.ResourceUsage.REQUESTER)
            @PathVariable String requester,
            @ApiParam(value = ApiDocumentation.ResourceUsage.PROVIDER)
            @PathVariable String provider,
            @ApiParam(value = ApiDocumentation.ResourceUsage.RESOURCE_TYPE)
            @PathVariable String resourceType,
            @ApiParam(value = ApiDocumentation.ResourceUsage.INITIAL_DATE)
            @PathVariable String initialDate,
            @ApiParam(value = ApiDocumentation.ResourceUsage.FINAL_DATE)
            @PathVariable String finalDate,
            @ApiParam(value = cloud.fogbow.common.constants.ApiDocumentation.Token.SYSTEM_USER_TOKEN)
            @RequestHeader(value = SystemConstants.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken
    ) throws Exception {
        try {
            List<Record> records = ApplicationFacade.getInstance().getUserRecords(userId, requester, provider,
                    resourceType, initialDate, finalDate, systemUserToken);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info(String.format(Messages.Exception.GENERIC_EXCEPTION, e.getMessage()), e);
            throw e;
        }
    }
    
    @RequestMapping(value = "/{userId}/{requester}/{provider}/{initialDate}/{finalDate}",
            method = RequestMethod.GET)
    public ResponseEntity<List<Record>> getAllResourcesUsageFromOtherUser(
            @ApiParam(value = ApiDocumentation.ResourceUsage.USER_ID) 
            @PathVariable String userId,
            @ApiParam(value = ApiDocumentation.ResourceUsage.REQUESTER)
            @PathVariable String requester,
            @ApiParam(value = ApiDocumentation.ResourceUsage.PROVIDER)
            @PathVariable String provider,
            @ApiParam(value = ApiDocumentation.ResourceUsage.INITIAL_DATE) 
            @PathVariable String initialDate,
            @ApiParam(value = ApiDocumentation.ResourceUsage.FINAL_DATE) 
            @PathVariable String finalDate,
            @ApiParam(value = cloud.fogbow.common.constants.ApiDocumentation.Token.SYSTEM_USER_TOKEN)
            @RequestHeader(value = SystemConstants.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken)
            throws Exception {
        try {
            List<Record> records = ApplicationFacade.getInstance().getAllResourcesUserRecords(userId, requester,
                    provider, initialDate, finalDate, systemUserToken);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info(String.format(Messages.Exception.GENERIC_EXCEPTION, e.getMessage()), e);
            throw e;
        }
    }

    @ApiOperation(value = ApiDocumentation.ResourceUsage.GET_OPERATION_FROM_SYSTEM_USER)
    @RequestMapping(value = "/{requester}/{resourceType}/{initialDate}/{finalDate}", method = RequestMethod.GET)
    public ResponseEntity<List<Record>> getResourceUsageFromSystemUser(
    		@ApiParam(value = ApiDocumentation.ResourceUsage.REQUESTER)
    		@PathVariable String requester,
    		@ApiParam(value = ApiDocumentation.ResourceUsage.RESOURCE_TYPE)
    		@PathVariable String resourceType,
    		@ApiParam(value = ApiDocumentation.ResourceUsage.INITIAL_DATE)
    		@PathVariable String initialDate,
    		@ApiParam(value = ApiDocumentation.ResourceUsage.FINAL_DATE)
    		@PathVariable String finalDate,
    		@ApiParam(value = cloud.fogbow.common.constants.ApiDocumentation.Token.SYSTEM_USER_TOKEN)
    		@RequestHeader(value = SystemConstants.SYSTEM_USER_TOKEN_HEADER_KEY) String systemUserToken
    ) throws Exception {
        try {
            List<Record> records = ApplicationFacade.getInstance().getSelfRecords(requester, resourceType, initialDate,
                finalDate, systemUserToken);
            return new ResponseEntity<>(records, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info(String.format(Messages.Exception.GENERIC_EXCEPTION, e.getMessage()), e);
            throw e;
        }
    }
}
