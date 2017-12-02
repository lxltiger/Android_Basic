package shine.com.basicrxjava;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.functions.Func1;
import shine.com.basicrxjava.entity.Beauty;
import shine.com.basicrxjava.entity.Pretty;

/**
 * Created by Administrator on 2016/7/25.
 */
public class BeautyToPretty implements Func1<Beauty,List<Pretty>>{
    @Override
    public List<Pretty> call(Beauty beauty) {

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.CHINA);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss",Locale.CHINA);
        List<Beauty.ResultsBean> results = beauty.getResults();
        List<Pretty> prettyList = new ArrayList<>();
        for (Beauty.ResultsBean result : results) {
            Pretty pretty=new Pretty();
            prettyList.add(pretty);
            pretty.url=result.getUrl();
            try {
                Date parse = inputFormat.parse(result.getCreatedAt());
                pretty.description=outputFormat.format(parse);
            } catch (ParseException e) {
                e.printStackTrace();
                pretty.description = "日期格式错误";
            }
        }
        return prettyList;
    }
}
