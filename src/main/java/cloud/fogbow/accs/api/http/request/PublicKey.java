package cloud.fogbow.accs.api.http.request;

import cloud.fogbow.accs.constants.Messages;
import cloud.fogbow.accs.constants.SystemConstants;
import cloud.fogbow.accs.core.ApplicationFacade;
import cloud.fogbow.common.constants.ApiDocumentation;
import cloud.fogbow.common.exceptions.UnexpectedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
@RequestMapping(value = PublicKey.PUBLIC_KEY_ENDPOINT)
@Api(description = ApiDocumentation.PublicKey.API)
public class PublicKey {
    public static final String PUBLIC_KEY_SUFFIX_ENDPOINT = "publicKey";
    public static final String PUBLIC_KEY_ENDPOINT = SystemConstants.SERVICE_BASE_ENDPOINT + PUBLIC_KEY_SUFFIX_ENDPOINT;

    private final Logger LOGGER = Logger.getLogger(PublicKey.class);

    @ApiOperation(value = ApiDocumentation.PublicKey.GET_OPERATION)
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<cloud.fogbow.accs.api.http.response.PublicKey> getPublicKey() throws UnexpectedException {
        try {
            String publicKeyValue = ApplicationFacade.getInstance().getPublicKey();
            cloud.fogbow.accs.api.http.response.PublicKey publicKey = new cloud.fogbow.accs.api.http.response.PublicKey(publicKeyValue);
            return new ResponseEntity<>(publicKey, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.info(String.format(Messages.Exception.GENERIC_EXCEPTION, e.getMessage()), e);
            throw e;
        }
    }

}
