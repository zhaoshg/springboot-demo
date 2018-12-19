package com.hlgf.sms.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 短信发送记录表
 *
 * @author ccr12312@163.com at 2018-10-24
 */
@Entity
@Getter
@Setter
@Data
@Table(name = "hl_sms_send_log")
@NoArgsConstructor
public class SmsSendLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sms_log_Sequence")
    @SequenceGenerator(name = "sms_log_Sequence", sequenceName = "sms_log_Sequence", allocationSize = 1)
    private Long id;

    /**
     * 接收者姓名
     */
    @Column(name = "receiver_name", columnDefinition = "varchar2(50)")
    private String receiverName;

    /**
     * 接收者号码
     */
    @Column(name = "receiver_number", columnDefinition = "varchar2(20)")
    private String receiverNumber;

    @Column(name = "pk_receiver", columnDefinition = "varchar2(20)")
    private String pkReceiver;

    /**
     * 发送时间,yyyy-MM-dd HH:mm:ss
     */
    @Column(name = "send_time", columnDefinition = "char(19)")
    private String sendTime;

    /**
     * 发送状态，成功与否,Y or N
     */
    @Column(name = "send_state", columnDefinition = "varchar2(2)")
    private String sendState;

    /**
     * 发送次数
     */
    @Column(name = "send_count", columnDefinition = "number(3)")
    private Integer sendCnt;

    /**
     * 发送内容
     */
    @Column(name = "content", columnDefinition = "varchar2(500)")
    private String content;

    /**
     * 发送字数
     */
    @Column(name = "word_count", columnDefinition = "number(3)")
    private Integer wordCount;

    /**
     * 相关单据号
     */
    @Column(name = "related_order", columnDefinition = "varchar2(20)")
    private String relatedOrder;

    /**
     * 相关单据Id
     */
    @Column(name = "related_id", columnDefinition = "varchar2(20)")
    private String relatedId;

    /**
     * 所在集团
     */
    @Column(name = "pk_group", columnDefinition = "varchar2(20)")
    private String pkGroup;

    /**
     * 所在集团名称
     */
    @Column(name = "group_name", columnDefinition = "varchar2(100)")
    private String groupName;

    /**
     * 短信类型
     */
    @Column(name = "sms_type", columnDefinition = "varchar2(20)")
    private String smsType;
}
