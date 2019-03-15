package accouting.services;

import cloud.fogbow.common.util.CryptoUtil;
import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.rmi.UnexpectedException;
import java.security.GeneralSecurityException;

import cloud.fogbow.common.util.ServiceAsymmetricKeysHolder;
import cloud.fogbow.common.util.CryptoUtil;

@Service
public class PublicKeyService {

    public String getPublicKey() throws UnexpectedException {
        try {
            return CryptoUtil.savePublicKey(ServiceAsymmetricKeysHolder.getInstance().getPublicKey());
        } catch (IOException | GeneralSecurityException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }

}
