package winemall.utils;

import java.util.Random;
import java.util.UUID;

/**
 * @Explain:  随机生成订单编号
 */
public class RandomUtil {
    public static String getRandomCode(Integer code) {
        Random random = new Random();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < code; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "").substring(0,12);
        return uuidStr;
    }
}
