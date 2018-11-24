package cn.linj2n.melody.web.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class ViewUtils {

    public String getFormatDate(ZonedDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(format);
    }

    public String renderToHtml(String markdown) {
        if (markdown == null || StringUtils.isEmpty(markdown)) {
            return "";
        }

        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

}
