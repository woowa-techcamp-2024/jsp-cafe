package woowa.camp.jspcafe.infra.time;

import java.time.LocalDate;

public interface DateTimeProvider {

    LocalDate getNow();

}
