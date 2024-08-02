package woowa.camp.jspcafe.infra.time;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DateTimeProvider {

    LocalDate getNow();

    LocalDateTime getNowAsLocalDateTime();

}
