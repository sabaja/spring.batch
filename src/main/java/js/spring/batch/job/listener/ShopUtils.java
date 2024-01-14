package js.spring.batch.job.listener;

import java.time.format.DateTimeFormatter;

public final class ShopUtils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final String DATETIME_ARG = "created-at";

    private ShopUtils() throws Exception {
        throw new Exception("");
    }

}
