package com.zebra.xconfig.server;

import com.zebra.xconfig.common.CommonUtil;
import com.zebra.xconfig.common.exception.XConfigException;
import com.zebra.xconfig.server.util.UserUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.springframework.web.util.HtmlUtils;

/**
 * Created by ying on 16/7/21.
 */
public class SimpleTest {

    @Test
    public void test(){
        System.out.println(CommonUtil.genKey("/mysql/dev/jdbc.drive"));
        System.out.println(CommonUtil.genXKeyByKey("mysql.test"));
    }

    @Test
    public void genPassword() throws Exception{
        String str = RandomStringUtils.random(10,true,true);
        System.out.println(str);
        System.out.println(UserUtil.genShaPassword("yukui.yin@alibaba-inc.com","123456",str));
    }

    @Test
    public void check(){
        try {
            CommonUtil.checkProjectProfileName(HtmlUtils.htmlEscape("zookeeper"));
        } catch (XConfigException e) {
            e.printStackTrace();
        }
    }
}
