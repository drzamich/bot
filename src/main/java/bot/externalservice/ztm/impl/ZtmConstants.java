package bot.externalservice.ztm.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ZtmConstants {
    public static final String BASE_URL = "http://www.nowa.wtp.waw.pl/rozklady-jazdy/";
    private static final String DATE_WTP_URL = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    public static final String AGGREGATE_PAGE_URL = BASE_URL + "?wtp_dt=" + DATE_WTP_URL + "&wtp_md=1";
    public static final String TIME_ZONE = "CET";
}
