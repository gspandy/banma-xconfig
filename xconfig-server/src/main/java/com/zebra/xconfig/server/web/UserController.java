package com.zebra.xconfig.server.web;

import com.zebra.xconfig.server.service.XUserService;
import com.zebra.xconfig.server.vo.AjaxResponse;
import com.zebra.xconfig.server.vo.Pagging;
import com.zebra.xconfig.server.vo.XUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by ying on 16/8/3.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final int pageSize = 30;

    @Resource
    private XUserService xUserService;

    @RequestMapping("/userList")
    public ModelAndView userList(WebRequest webRequest){
        ModelAndView mv = new ModelAndView();
        String userName = webRequest.getParameter("un");
        String pageNumStr = webRequest.getParameter("pn");

        int pageNum;
        try{
            pageNum = Integer.valueOf(pageNumStr);
        }catch (NumberFormatException e){
            pageNum = 0;
        }
        pageNum = pageNum-1 < 0 ? 0 : pageNum-1;

        Pagging<XUserVo> users = this.xUserService.queryUsersByUserName(userName,pageNum,pageSize);

        mv.getModel().put("users",users);
        mv.getModel().put("un",userName == null ? "" : userName);
        mv.setViewName("page/userList.ftl");
        return mv;
    }

    @RequestMapping("/addUser")
    @ResponseBody
    public AjaxResponse addUser(WebRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();
        try{
            String userName = webRequest.getParameter("userName");
            String passwrod = webRequest.getParameter("password");
            String userNike = webRequest.getParameter("userNike");
            String role = webRequest.getParameter("role");

            this.xUserService.addUser(userName,passwrod,userNike,Integer.valueOf(role));

        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }
        return ajaxResponse;
    }

    @RequestMapping("/removeUser")
    @ResponseBody
    public AjaxResponse removeUser(WebRequest webRequest){
        AjaxResponse ajaxResponse = new AjaxResponse();
        try{
            String userName = webRequest.getParameter("userName");

            this.xUserService.removeUser(userName);

        }catch (Exception e){
            logger.error(e.getMessage(),e);
            ajaxResponse.setThrowable(e);
        }
        return ajaxResponse;
    }
}