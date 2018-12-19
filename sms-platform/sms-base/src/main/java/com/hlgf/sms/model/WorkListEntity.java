package com.hlgf.sms.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author ccr12312@163.com at 2018-10-25
 */
@Setter
@Getter
public class WorkListEntity implements SmsLogCapable {
    /**
     * sm_msg_content 主键
     */
    private String pkMessage;

    /**
     * 工作任务主题
     */
    private String subject;

    /**
     * 接受者主键
     */
    private String pkReceiver;

    /**
     * 接收者用户名
     */
    private String userName;

    /**
     * 接收者姓名
     */
    private String receiverName;

    /**
     * 接收者手机
     */
    private String mobile;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 是否已处理
     */
    private String isHandled;

    /**
     * 集团主键
     */
    private String pkGroup;
    /**
     * 集团名称
     */
    private String groupName;



    private String openId;

    @Override
    public SmsSendLog toSmsSendLog() {
        SmsSendLog log = new SmsSendLog();
        log.setReceiverName(this.getReceiverName());
        log.setReceiverNumber(this.getMobile());
        log.setPkReceiver(this.getPkReceiver());
        log.setPkGroup(this.getPkGroup());
        log.setGroupName(this.getGroupName());
        log.setRelatedId(this.getPkMessage());
        return log;
    }
}
