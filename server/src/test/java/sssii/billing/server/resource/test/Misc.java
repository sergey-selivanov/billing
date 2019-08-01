package sssii.billing.server.resource.test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class Misc {

    @Test
    public void test() {

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        //OffsetDateTime odt = OffsetDateTime.parse("2010-04-27 03:29:31.1630867 -07:00");
        OffsetDateTime odt = OffsetDateTime.parse("2010-04-27T03:29:31.1630867-07:00");

        System.out.println("odt     : " + odt);

        Instant i = odt.atZoneSameInstant(ZoneOffset.UTC).toInstant();
        System.out.println("instant : " + i);

        Instant i2 = odt.toInstant();
        System.out.println("instant2: " + i2);

        Date d = Date.from(i);
        System.out.println("date    : " + d);

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("+00:00"));    // "UTC"
        System.out.println("cal     : " + c.getTime());

        Calendar c2 = new Calendar.Builder().setInstant(d).setTimeZone(TimeZone.getTimeZone("+00:00")).build();
        System.out.println("cal2    : " + c2.getTime());

        Timestamp ts = Timestamp.from(i);

        System.out.println("tstamp  : " + ts);
    }

    @Test
    public void testFormat() {
        BigDecimal bd = new BigDecimal(123435678.1234);
        String format = "$ %.2f";
        System.out.println(String.format(format, bd));

        format = "$ %,.2f";
        System.out.println(String.format(Locale.FRANCE, format, bd));

        // https://stackoverflow.com/questions/10826850/java-string-format-with-currency-symbol

        System.out.println(DecimalFormat.getCurrencyInstance().format(bd));

        System.out.println(DecimalFormat.getCurrencyInstance(Locale.FRANCE).format(bd));
        System.out.println(DecimalFormat.getCurrencyInstance(
                new Locale.Builder().setLanguage("uk").setRegion("ua").build())
                .format(bd));
        System.out.println(DecimalFormat.getCurrencyInstance(
                new Locale.Builder().setLanguage("ru").setRegion("ru").build())
                .format(bd));

        System.out.println(DecimalFormat.getCurrencyInstance(Locale.forLanguageTag("uk-UA")).format(bd));
        System.out.println(DecimalFormat.getCurrencyInstance(Locale.forLanguageTag("ru-RU")).format(bd));
    }

    @Test
    public void testRegex() {
        Pattern p = Pattern.compile("(filename=\")(.*)(\")");
        Matcher m = p.matcher("Content-Disposition: attachment; filename=\"gferg.pdf\"");
        m.find();
        System.out.println(m.group(2));
    }
}
