package com.diga.orm.resolver;

import com.diga.orm.common.WorkCommon;
import com.diga.orm.pojo.work.User;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserArgumentsResolver implements HandlerMethodArgumentResolver {

    /**
     * 处理 Controller 方法中参数类型为 com.diga.orm.pojo.work.User 的参数
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType() == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest req = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        // HttpServletResponse resp = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        HttpSession session = req.getSession();
        Object user = session.getAttribute(WorkCommon.CURRENT_USER);

        // 如果不存在, 将这个参数设置为 null 就可以了
        return user;
    }
}
