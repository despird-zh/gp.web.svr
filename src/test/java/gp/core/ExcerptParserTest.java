package gp.core;

import com.gp.web.common.PostParser;
import com.gp.web.util.ExcerptUtils;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by garydiao on 8/1/16.
 */
@ContextConfiguration(
classes={
        com.gp.config.RootConfigurer.class,
        com.gp.config.ServiceConfigurer.class
})
public class ExcerptParserTest extends AbstractJUnit4SpringContextTests {

    @Test
    public void testParser(){

        String hcode  = "<p>please remind of following things<br></p><ol><li>submit letter template to alvado</li><li>delete table redundant field</li><li>truncate the table<br></li></ol>\n" +
                "<img src=\"data:image/gif;base64,R0lGODlhDwAPAKECAAAAzMzM/////  \n" +
                "wAAACwAAAAADwAPAAACIISPeQHsrZ5ModrLlN48CXF8m2iQ3YmmKqVlRtW4ML  \n" +
                "wWACH+H09wdGltaXplZCBieSBVbGVhZCBTbWFydFNhdmVyIQAAOw==\"  \n" +
                "alt=\"Base64 encoded image\" width=\"150\" height=\"150\"/>\n" +
                "<img src=\"data:image/gif;base64,R0lGODlhDwAPAKECAAAAzMzM/////  \n" +
                "wAAACwAAAAADwAPAAACIISPeQHsrZ5ModrLlN48CXF8m2iQ3YmmKqVlRtW4ML  \n" +
                "wWACH+H09wdGltaXplZCBieSBVbGVhZCBTbWFydFNhdmVyIQAAOw==\"  \n" +
                "alt=\"Base64 encoded image\" width=\"150\" height=\"150\"/> ";

        PostParser pp = new PostParser(hcode);
        System.out.println(pp.getPostContent());
        System.out.println(pp.getPostExcerpt());
        System.out.println(pp.getPostImages().toString());
    }
}
