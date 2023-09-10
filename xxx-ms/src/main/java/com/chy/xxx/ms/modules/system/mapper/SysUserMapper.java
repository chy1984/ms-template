package com.chy.xxx.ms.modules.system.mapper;

import com.chy.xxx.ms.modules.system.po.SysUserPo;
import com.chy.xxx.ms.modules.system.qo.SysUserQo;
import com.chy.xxx.ms.modules.system.vo.req.SysUserAddReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysUserPageReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysUserUpdateReqVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysUserPageRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysUserRespVo;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * 系统用户mapper
 *
 * @author chy
 */
@Mapper(componentModel = SPRING)
public interface SysUserMapper {

    SysUserPo addReqVoToPo(SysUserAddReqVo reqVo);

    SysUserPo updateReqVoToPo(SysUserUpdateReqVo reqVo);

    SysUserQo pageReqVoToQo(SysUserPageReqVo reqVo);

    List<SysUserPageRespVo> posToPageRespVos(List<SysUserPo> pos);

    List<SysUserRespVo> posToRespVos(List<SysUserPo> pos);

}
