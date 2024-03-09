package commons;

import java.security.SecureRandom;
import java.util.EnumSet;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;
import org.hibernate.exception.ConstraintViolationException;

public class InviteCodeGenerator implements BeforeExecutionGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 8;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private String generateUniqueCode() {
        StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = SECURE_RANDOM.nextInt(CHARACTERS.length());
            codeBuilder.append(CHARACTERS.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o, Object o1, EventType eventType) {
        //Method that runs when generate is called from hibernate
        while (true) {
            try {
                return generateUniqueCode();
            } catch (ConstraintViolationException ex) {
                // If a constraint violation occurs, retry generation
            }
        }
    }
    @Override
    public EnumSet<EventType> getEventTypes() {
        return EventTypeSets.INSERT_ONLY;
    }
}