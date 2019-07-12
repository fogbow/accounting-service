package cloud.fogbow.accs.api.http.request;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cloud.fogbow.accs.api.http.request.Version;
import cloud.fogbow.accs.constants.Messages;
import cloud.fogbow.accs.constants.SystemConstants;
import cloud.fogbow.accs.core.ApplicationFacade;
import cloud.fogbow.common.constants.ApiDocumentation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping(value = Version.VERSION_ENDPOINT)
@Api(description = ApiDocumentation.Version.API)
public class Version {

	public static final String VERSION_ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + "version";

    private final Logger LOGGER = Logger.getLogger(Version.class);

    @ApiOperation(value = ApiDocumentation.Version.GET_OPERATION)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<cloud.fogbow.accs.api.http.response.Version> getVersion() {

        LOGGER.info(Messages.Info.RECEIVING_GET_VERSION_REQUEST);
        String versionNumber = ApplicationFacade.getInstance().getVersionNumber();
        cloud.fogbow.accs.api.http.response.Version version = new cloud.fogbow.accs.api.http.response.Version(versionNumber);
        return new ResponseEntity<>(version, HttpStatus.OK);
    }
}
