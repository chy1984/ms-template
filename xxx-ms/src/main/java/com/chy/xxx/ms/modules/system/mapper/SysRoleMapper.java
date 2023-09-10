package com.chy.xxx.ms.modules.system.mapper;

import com.chy.xxx.ms.modules.system.po.SysRolePo;
import com.chy.xxx.ms.modules.system.qo.SysRoleQo;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleAddReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRolePageReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleUpdateReqVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysRolePageRespVo;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * 系统角色mapper
 *
 * @author chy
 */
@Mapper(componentModel = SPRING)
public interface SysRoleMapper {

    SysRolePo addReqVoToPo(SysRoleAddReqVo reqVo);

    SysRolePo updateReqVoToPo(SysRoleUpdateReqVo reqVo);

    SysRoleQo pageReqVoToQo(SysRolePageReqVo reqVo);

    List<SysRolePageRespVo> posToPageRespVos(List<SysRolePo> pos);

}
