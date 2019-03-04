package com.aquaman.security.admin.service;

import com.aquaman.security.admin.entity.domain.Role;
import com.aquaman.security.admin.entity.query.RoleQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wei wang
 * @since 2019-03-04
 */
public interface IRoleService extends IService<Role> {

    /**
     * 角色分页查询
     * @param query
     * @return
     */
    IPage<Role> page(RoleQuery query);

}
