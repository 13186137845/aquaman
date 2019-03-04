/*
MIT License

Copyright (c) 2019 wei wang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.aquaman.security.admin.handler.login;

import com.aquaman.security.admin.entity.domain.User;
import com.aquaman.security.admin.entity.vo.CurrentLoginUserVO;
import com.aquaman.security.admin.entity.vo.ResultVO;
import com.aquaman.security.admin.exception.PrincipalToUserConversionException;
import com.aquaman.security.admin.service.IUserService;
import com.aquaman.security.common.constant.AquamanConstant;
import com.aquaman.security.common.util.JSONUtil;
import com.aquaman.security.common.enums.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.IOException;

/**
 * 登录成功后spring security将会调用该处理器
 * @author 创建者 wei.wang
 * @author 修改者 wei.wang
 * @version 2019/2/28
 * @since 2019/2/28
 */
@Component("loginSuccessHandler")
@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private IUserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        // 获取登录成功对象
        if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof User){
            User user = (User) authentication.getPrincipal();
            // 组装登录会员VO
            CurrentLoginUserVO currentLoginUserVO = new CurrentLoginUserVO();
            currentLoginUserVO.setAccount(user.getAccount());
            // 组装返回VO
            ResultVO resultVO = new ResultVO(ResultCodeEnum.SUCCESS, user);
            resultVO.setData(currentLoginUserVO);
            // JSON格式返回
            response.getWriter().print(JSONUtil.objectToJSONString(resultVO));
            // 记录最后登录时间，就算失败也吃掉异常，记录错误日志
            try{
                userService.updateLoginTime(user.getId());
            }catch (Exception ex) {
                log.error("{}记录登录时间失败:{}-{}-{}", AquamanConstant.LOG_TAG,
                    ResultCodeEnum.INERT_LOGIN_TIME_ERROR.getCode(), ResultCodeEnum.INERT_LOGIN_TIME_ERROR.getMsg(), ex.getMessage());
            }
        } else {
            log.error("{}Principal转换异常{}", AquamanConstant.LOG_TAG, AquamanConstant.LOG_TAG);
            // 如果authentication.getPrincipal()为空或authentication.getPrincipal()无法转换成User，抛出异常
            ResultVO resultVO = new ResultVO(ResultCodeEnum.PRINCIPAL_TO_USER_CONVERSION);
            throw new PrincipalToUserConversionException(resultVO);
        }
    }
}
