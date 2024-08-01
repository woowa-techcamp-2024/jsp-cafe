package woowa.camp.jspcafe.infra.time;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateTimeProvider implements DateTimeProvider {

    @Override
    public LocalDate getNow() {
        return LocalDate.now();
    }

    @Override
    public LocalDateTime getNowAsLocalDateTime() {
        return LocalDateTime.now();
    }

}
