package woowa.camp.jspcafe.utils;

import java.time.LocalDate;
import woowa.camp.jspcafe.infra.time.DateTimeProvider;

public class FixedDateTimeProvider implements DateTimeProvider {

    LocalDate fixedTime;

    public FixedDateTimeProvider(int year, int month, int day) {
        fixedTime = LocalDate.of(year, month, day);
    }

    @Override
    public LocalDate getNow() {
        return fixedTime;
    }

}

