package com.jnm.Tutor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jnm.Tutor.mapper.OrdersMapper;
import com.jnm.Tutor.model.Orders;
import com.jnm.Tutor.service.OrdersService;
import org.springframework.stereotype.Service;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
}