package com.chy.xxx.ms.modules.system.service.business;

import com.chy.xxx.ms.modules.system.vo.req.*;
import com.chy.xxx.ms.modules.system.vo.resp.SysUserDetailRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysUserPageRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysUserTokenRespVo;
import com.chy.xxx.ms.response.CommonPage;

/**
 * 系统用户业务service
 *
 * @author chy
 */
public interface SysUserService {

    /**
     * 登录
     *
     * @param reqVo 请求参数
     * @return SysUserLoginRespVo
     */
    SysUserTokenRespVo login(SysUserLoginReqVo reqVo);

    /**
     * 登出
     *
     * @param username 用户名
     */
    void logout(String username);

    /**
     * 获取用户详情
     *
     * @param username 用户名
     * @return SysUserDetailRespVo
     */
    SysUserDetailRespVo getUserDetail(String username);

    /**
     * 获取用户详情
     *
     * @param token 当前token
     * @return SysUserTokenRespVo
     */
    SysUserTokenRespVo refreshToken(String token);

    /**
     * 添加系统用户
     *
     * @param reqVo 请求参数
     */
    void add(SysUserAddReqVo reqVo);

    /**
     * 更新系统用户
     *
     * @param reqVo 请求参数
     */
    void update(SysUserUpdateReqVo reqVo);

    /**
     * 删除系统用户
     *
     * @param id 主键id
     */
    void delete(Long id);

    /**
     * 分页查询系统用户信息
     *
     * @param reqVo 请求参数
     * @return CommonPage<SysUserRespVo>
     */
    CommonPage<SysUserPageRespVo> page(SysUserPageReqVo reqVo);

    /**
     * 充值用户密码
     *
     * @param id 主键id
     */
    void resetPassword(Long id);

    /**
     * 更新密码
     *
     * @param reqVo 请求参数
     */
    void updatePassword(SysUserUpdatePasswordReqVo reqVo);

}

