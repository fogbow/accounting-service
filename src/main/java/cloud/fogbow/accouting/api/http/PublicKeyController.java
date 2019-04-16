package cloud.fogbow.accouting.api.http;

import cloud.fogbow.accouting.models.PublicKey;
import cloud.fogbow.accouting.services.PublicKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.UnexpectedException;


@CrossOrigin
@RestController
@RequestMapping(value = PublicKeyController.PUBLIC_KEY_ENDPOINT)
public class PublicKeyController {
    public static final String PUBLIC_KEY_ENDPOINT = "publicKey";

    @Autowired
    private PublicKeyService publicKeyService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PublicKey> getPublicKey() throws UnexpectedException {
        String publicKeyValue = publicKeyService.getPublicKey();
        PublicKey publicKey = new PublicKey(publicKeyValue);
        return new ResponseEntity<>(publicKey, HttpStatus.OK);
    }

}
