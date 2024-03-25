package com.vecondev.buildoptima.manager;

import static com.vecondev.buildoptima.exception.Error.FAILED_KEY_READ;

import com.vecondev.buildoptima.config.properties.JwtConfigProperties;
import com.vecondev.buildoptima.config.properties.SecretsManagerConfigProperties;
import com.vecondev.buildoptima.exception.KeypairException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class CertificateManager {

  private final JwtConfigProperties jwtConfigProperties;
  private final SecretsManagerConfigProperties secretsManagerConfigProperties;

  public PrivateKey privateKey() {
    try {
      return KeyFactory.getInstance(jwtConfigProperties.getSignatureAlgorithm().getFamilyName())
          .generatePrivate(
              new PKCS8EncodedKeySpec(
                  Base64.decode(secretsManagerConfigProperties.getPrivateKeySecret())));
    } catch (Exception ex) {
      throw new KeypairException(FAILED_KEY_READ);
    }
  }
}
