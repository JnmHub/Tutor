package com.jnm.Tutor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jnm.Tutor.model.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}