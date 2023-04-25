package com.biosphere.adminmodule.controller;


import com.alibaba.fastjson.JSONObject;
import com.biosphere.adminmodule.service.IAdministratorService;
import com.biosphere.library.vo.CommunityPostVo;
import com.biosphere.library.vo.ExceptionLogVo;
import com.biosphere.library.vo.RespBeanEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hyh
 * @since 2023-04-18
 */
@Api(tags = "管理员模块")
@Slf4j
@RestControllerAdvice
@CrossOrigin
@Validated
@RequestMapping("/admin")
public class AdministratorController {


    @Autowired
    private IAdministratorService administratorService;

    @ApiOperation(value = "管理员帖子管理加载", notes = "")
    @RequestMapping(value = "/auth/loadPostList",method = RequestMethod.GET)
    public JSONObject loadPostList(){
        JSONObject resData = new JSONObject();

        List<CommunityPostVo> communityPostVos = administratorService.loadPost();
        if (Objects.isNull(communityPostVos)) {
            throw new ExceptionLogVo(RespBeanEnum.LOAD_POST_ERROR);
        }
        resData.put("postList", communityPostVos);

        return resData;
    }

    @ApiOperation(value = "管理员帖子管理加载", notes = "")
    @RequestMapping(value = "/exposure/login",method = RequestMethod.GET)
    public JSONObject login(@Validated @NotBlank String account, @Validated @NotBlank String pwd){
        JSONObject resData = new JSONObject();
        String token = administratorService.login(account, pwd);
        resData.put("token", token);
        return resData;
    }

}
