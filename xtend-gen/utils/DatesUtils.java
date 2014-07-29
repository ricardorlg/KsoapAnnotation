package utils;

import com.google.common.collect.Lists;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class DatesUtils {
  private final static List<String> formats = Collections.<String>unmodifiableList(Lists.<String>newArrayList("yyyy-MM-dd\'T\'HH:mm:ss.SSS", "yyyy-MM-dd\'T\'HH:mm:ss.SSSXXX", "yyyy-MM-dd\'T\'HH:mm:ss", "yyyy-MM-dd\'T\'HH:mm", "yyyy-MM-dd", "dd/mm/yyyy"));
  
  public static Date parses(final String dateToFormat) {
    for (final String format : DatesUtils.formats) {
      try {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        return formater.parse(dateToFormat);
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception e = (Exception)_t;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
    return null;
  }
}
