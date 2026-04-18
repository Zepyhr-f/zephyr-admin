package com.zephyr.rule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.zephyr.rule.mapper.AmlTransactionMapper;
import com.zephyr.rule.pojo.entity.AmlTransaction;
import com.zephyr.rule.service.IAmlTransactionService;

/**
* 交易流水Service实现
*
* @author zephyr
* @since 2025-10-13
*/
@Service
public class AmlTransactionServiceImpl extends ServiceImpl<AmlTransactionMapper, AmlTransaction> implements IAmlTransactionService {

}
