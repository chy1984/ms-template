package com.chy.xxx.ms.modules.system.mapper;

import com.chy.xxx.ms.modules.system.po.SysResourcePo;
import com.chy.xxx.ms.modules.system.qo.SysResourceQo;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceAddReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceListReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceUpdateReqVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysResourceRespVo;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

/**
 * 系统资源mapper
 *
 * @author chy
 */
@Mapper(componentModel = SPRING)
public interface SysResourceMapper {

    List<SysResourceRespVo> posToRespVos(List<SysResourcePo> pos);

    SysResourcePo addReqVoToPo(SysResourceAddReqVo reqVo);

    SysResourcePo updateReqVoToPo(SysResourceUpdateReqVo reqVo);

    SysResourceQo listReqVoToQo(SysResourceListReqVo reqVo);

}
