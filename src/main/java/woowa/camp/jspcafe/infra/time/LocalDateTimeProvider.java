package woowa.camp.jspcafe.infra.time;

import java.time.LocalDate;

public class LocalDateTimeProvider implements DateTimeProvider {

    @Override
    public LocalDate getNow() {
        return LocalDate.now();
    }

}
