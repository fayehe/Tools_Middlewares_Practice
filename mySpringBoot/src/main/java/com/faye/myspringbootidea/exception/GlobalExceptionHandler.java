package com.faye.myspringbootidea.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//@ControllerAdvice是一个@Component，用于定义@ExceptionHandler，@InitBinder和@ModelAttribute方法，适用于所有使用@RequestMapping方法。
//Spring4之前，@ControllerAdvice在同一调度的Servlet中协助所有控制器。Spring4已经改变：@ControllerAdvice支持配置控制器的子集，而默认的行为仍然可以利用。
//在Spring4中， @ControllerAdvice通过annotations(), basePackageClasses(), basePackages()方法定制用于选择控制器子集。
//不过据经验之谈，只有配合@ExceptionHandler最有用，其它两个不常用。
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("errorPage");
        return mav;
    }
}
