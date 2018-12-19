package com.hlgf.sms.model;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;

/**
 * @author ccr12312@163.com at 2018-11-5
 */
@Entity
@Table(name = "hl_sms_send_setting")
@Setter
@Getter
@NoArgsConstructor
public class SmsSendSetting {

    /**
     * 工作任务提醒
     */
    public static int WORK_LIST_REMIND = 1;
    /**
     * 预警通知
     */
    public static int PRE_ALERT_REMIND = 1 << 1;

    /**
     * 到款通知
     */
    public static int FUNDS_ARRIVALS_NOTIFICATION = 1 << 2;

    /**
     * 销售单通知
     */
    public static int SALES_ORDER_NOTIFICATION = 1 << 3;

    /**
     * 余额提醒
     */
    public static int BALANCE_REMIND = 1 << 4;

    /**
     * 所在集团
     */
    @Id
    @Column(name = "pk_group", columnDefinition = "varchar2(20)")
    private String pkGroup;

    /**
     * 所在集团名称
     */
    @Column(name = "group_name", columnDefinition = "varchar2(100)")
    private String groupName;

    /**
     * 公司对应的开通功能
     * 由WORK_LIST_REMIND，FUNDS_ARRIVALS_NOTIFICATION，SALES_ORDER_NOTIFICATION，BALANCE_REMIND组成
     */
    @Column(name = "state", columnDefinition = "number(3)")
    private int state;

}
