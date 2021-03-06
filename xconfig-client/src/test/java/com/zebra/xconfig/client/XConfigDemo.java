package com.zebra.xconfig.client;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Created by ying on 16/8/17.
 */
public class XConfigDemo {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String PROJECT = "demo";
    private final String DEMO_CONFIG = "demo.configKey";
    private final String DEMO_WATCH = "demo.watchKey";

    @Test
    public void test(){
        try {
            XConfig xConfig = XConfigFactory.instance(PROJECT);
//            XConfigFactory.instance("base-mysql");

            logger.info("===>project:{},profile:{}",xConfig.getProject(),XConfigFactory.getProfile());

            logger.info("===>{}:{}",DEMO_CONFIG,XConfig.getValue(DEMO_CONFIG));
            logger.info("===>{}:{}",DEMO_WATCH,XConfig.getValue(DEMO_WATCH));

            //最佳实践
            Thread t = new Thread(){
                @Override
                public void run() {
                    while(true) {
                        try {
                            Thread.sleep(1000 * 2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        logger.info("====thread===>{}:{}", DEMO_WATCH, XConfig.getValue(DEMO_WATCH));
                    }
                }
            };
            t.start();

            //注册监听
            XConfig.addObserver(new XKeyObserver() {
                @Override
                public String getKey() {
                    return DEMO_WATCH;
                }

                @Override
                public void change(String value) {
                    logger.info("====change===>{}:{}",DEMO_WATCH,XConfig.getValue(DEMO_WATCH));
                }
            });

            Thread.sleep(1000*60*30);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test2XConfig(){
        try {
            //1
            XConfig xConfig = XConfigFactory.instance(PROJECT);

            logger.info("===>project:{},profile:{}", xConfig.getProject(), XConfigFactory.getProfile());

            logger.info("===>{}:{}",DEMO_CONFIG,XConfig.getValue(DEMO_CONFIG));
            logger.info("===>{}:{}",DEMO_WATCH,XConfig.getValue(DEMO_WATCH));

            //注册监听
            XConfig.addObserver(new XKeyObserver() {
                @Override
                public String getKey() {
                    return DEMO_WATCH;
                }

                @Override
                public void change(String value) {
                    logger.info("====change===>{}:{}",DEMO_WATCH,XConfig.getValue(DEMO_WATCH));
                }
            });

            //2
            XConfigFactory.instance("odps");
            logger.info("===>{}:{}","odps.endpoint.inner",XConfig.getValue("odps.endpoint.inner"));
            xConfig.addObserver(new XKeyObserver() {
                @Override
                public String getKey() {
                    return "odps.endpoint.inner";
                }

                @Override
                public void change(String value) {
                    logger.info("====change===>{}:{}","odps.endpoint.inner",XConfig.getValue("odps.endpoint.inner"));
                }
            });

            Thread.sleep(1000*60*30);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testSpring() throws Exception{

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(new String[]{"classpath:spring-demo.xml"});
        classPathXmlApplicationContext.registerShutdownHook();
        classPathXmlApplicationContext.start();

        logger.info("===>{}:{}",DEMO_CONFIG,XConfig.getValue(DEMO_CONFIG));
        logger.info("===>{}:{}",DEMO_WATCH,XConfig.getValue(DEMO_WATCH));

        XConfigFactory.instance("demo", new XConfigInitListener() {
            @Override
            public void complete(XConfig xConfig) {
                System.out.println("====================xconfig is ok============");
            }
        });

        //最佳实践
        Thread t = new Thread(){
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000 * 2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    logger.info("====thread===>{}:{}", DEMO_WATCH, XConfig.getValue(DEMO_WATCH));
                }
            }
        };
        t.start();

        //注册监听
        XConfig.addObserver(new XKeyObserver() {
            @Override
            public String getKey() {
                return DEMO_WATCH;
            }

            @Override
            public void change(String value) {
                logger.info("====change===>{}:{}",DEMO_WATCH,XConfig.getValue(DEMO_WATCH));
            }
        });

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
