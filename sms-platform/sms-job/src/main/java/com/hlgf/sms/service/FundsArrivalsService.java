package com.hlgf.sms.service;

import com.hlgf.sms.model.FundsArrivalsEntity;

import java.util.List;

/**
 * @author ccr12312@163.com at 2018-11-5
 */
public interface FundsArrivalsService {

    List<FundsArrivalsEntity> findFundsArrivalsPassedRecently(String timeLimit);
}
