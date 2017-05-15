package eu.rubengrab.services;

import eu.rubengrab.model.SmartLockBundle;
import eu.rubengrab.model.SmartLockHouseDescription;
import eu.rubengrab.model.User;
import eu.rubengrab.repositories.SmartLockRepository;
import eu.rubengrab.utils.Encrypter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

/**
 * Created by Ruben on 11.05.2017.
 */
@Service
public class SmartLockService {

    @Autowired
    private SmartLockRepository smartLockRepository;


    public SmartLockBundle getEncryptedUnlockKey(User user, Long major, Long minor, String uuid) {

        String unlockCode = smartLockRepository.getUnlockCode(major, minor, uuid);
        String pepper = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_YEAR));

        String plainText = unlockCode + pepper;
        String cypherText = "";
        String address = smartLockRepository.getAddress(major, minor, uuid);
        try {
            cypherText = Encrypter.bytesToHex(Encrypter.encrypt(plainText));
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new SmartLockBundle(cypherText, address);
    }

    public SmartLockHouseDescription getSmartLockDescription(Long major, Long minor, String uuid) {
        return smartLockRepository.getDescription(major,minor,uuid);
    }
}
