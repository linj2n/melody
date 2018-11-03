package cn.linj2n.melody.web.utils;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ViewUtils {

    public String getFormatDate(ZonedDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        return dateTime.format(format);
    }

}
