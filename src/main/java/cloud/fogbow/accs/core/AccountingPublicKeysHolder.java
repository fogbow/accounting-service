package cloud.fogbow.accs.core;

import cloud.fogbow.accs.constants.ConfigurationPropertyKeys;
import cloud.fogbow.common.constants.HttpMethod;
import cloud.fogbow.common.exceptions.ConfigurationErrorException;
import cloud.fogbow.common.exceptions.FogbowException;
import cloud.fogbow.common.exceptions.UnavailableProviderException;
import cloud.fogbow.common.exceptions.InternalServerErrorException;
import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.connectivity.HttpRequestClient;
import cloud.fogbow.common.util.connectivity.HttpResponse;
import com.google.gson.Gson;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

public class AccountingPublicKeysHolder {
    private RSAPublicKey asPublicKey;

    private static AccountingPublicKeysHolder instance;

    private AccountingPublicKeysHolder() {
    }

    public static synchronized AccountingPublicKeysHolder getInstance() {
        if (instance == null) {
            instance = new AccountingPublicKeysHolder();
        }
        return instance;
    }

    public RSAPublicKey getAsPublicKey() throws FogbowException {
        if (this.asPublicKey == null) {
            String asAddress = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.AS_URL_KEY);
            String asPort = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.AS_PORT_KEY);
            this.asPublicKey = getPublicKey(asAddress, asPort, cloud.fogbow.as.api.http.request.PublicKey.PUBLIC_KEY_ENDPOINT);
        }
        return this.asPublicKey;
    }

    private RSAPublicKey getPublicKey(String serviceAddress, String servicePort, String suffix) throws FogbowException {
        RSAPublicKey publicKey = null;

        URI uri = null;
        try {
            uri = new URI(serviceAddress);
        } catch (URISyntaxException e) {
            throw new ConfigurationErrorException(String.format("Invalid URL", serviceAddress));
        }
        uri = UriComponentsBuilder.fromUri(uri).port(servicePort).path(suffix).build(true).toUri();

        String endpoint = uri.toString();
        HttpResponse response = HttpRequestClient.doGenericRequest(HttpMethod.GET, endpoint, new HashMap<>(), new HashMap<>());
        if (response.getHttpCode() > HttpStatus.SC_OK) {
            Throwable e = new HttpResponseException(response.getHttpCode(), response.getContent());
            throw new UnavailableProviderException(e.getMessage());
        } else {
            try {
                Gson gson = new Gson();
                Map<String, String> jsonResponse = gson.fromJson(response.getContent(), HashMap.class);
                //TODO: the key should be a constant defined elsewhere; this class is a candidate to go to common
                String publicKeyString = jsonResponse.get("publicKey");
                publicKey = CryptoUtil.getPublicKeyFromString(publicKeyString);
            } catch (GeneralSecurityException e) {
                throw new InternalServerErrorException("Invalid URL ");
            }
            return publicKey;
        }
    }
}

