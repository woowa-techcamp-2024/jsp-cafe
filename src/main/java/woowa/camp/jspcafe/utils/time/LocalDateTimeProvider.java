package woowa.camp.jspcafe.utils.time;

import java.time.LocalDate;

public class LocalDateTimeProvider implements DateTimeProvider {

    @Override
    public LocalDate getNow() {
        return LocalDate.now();
    }

}
