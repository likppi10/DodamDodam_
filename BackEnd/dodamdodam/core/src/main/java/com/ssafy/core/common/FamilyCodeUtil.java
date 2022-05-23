package com.ssafy.core.common;

import com.ssafy.core.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class FamilyCodeUtil {

    private final FamilyRepository familyRepository;
    private final Random random = new SecureRandom();

    public String createFamilyCode(){
        StringBuilder key = new StringBuilder();

        do {
            for (int j = 0; j < 15; j++) {
                if (random.nextBoolean()) {
                    key.append((char) (random.nextInt(26) + 65));
                } else {
                    key.append(random.nextInt(10));
                }
            }
        } while (familyRepository.findFamilyByCode(key.toString()) != null);

        return key.toString();
    }
}
