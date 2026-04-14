package util;
/**
 * 
 * @author SooZZ
 * @param <T> 
 */
public final class IdGenerator {
    private IdGenerator() {}

    public static String nextId(int currentCount) {
        int n = 1000 + currentCount + 1;
        return "m" + n;
    }

    public static int parseNumber(String id) {
        if (id == null || id.length() < 2 || (id.charAt(0) != 'm' && id.charAt(0) != 'M'))
            throw new IllegalArgumentException("Invalid id: " + id);
        return Integer.parseInt(id.substring(1));
    }
}
