package com.jnm.Tutor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jnm.Tutor.model.Admins;
import com.jnm.Tutor.model.dto.admin.AdminCreateDTO;
import com.jnm.Tutor.model.dto.admin.AdminUpdateDTO;

public interface AdminsService extends IService<Admins> {
    /**
     * 创建一个新的管理员账户
     * @param dto 包含管理员信息的DTO
     */
    void createAdmin(AdminCreateDTO dto);

    /**
     * 更新一个管理员账户
     * @param id 要更新的管理员ID
     * @param dto 包含更新信息的DTO
     */
    void updateAdmin(String id, AdminUpdateDTO dto);
}