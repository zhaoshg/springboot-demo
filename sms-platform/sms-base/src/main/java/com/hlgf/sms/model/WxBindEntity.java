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
@Table(name = "hl_wx_bind")
@NoArgsConstructor
public class WxBindEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wx_bind_Sequence")
    @SequenceGenerator(name = "wx_bind_Sequence", sequenceName = "wx_bind_Sequence", allocationSize = 1)
    private Long id;

    /**
     * nick_name
     */
    @Column(name = "nick_name", columnDefinition = "varchar2(150)")
    private String nickName;

    /**
     * 接收者号码
     */
    @Column(name = "phone", columnDefinition = "varchar2(20)")
    private String phone;

    /**
     * 微信的openId
     */
    @Column(name = "open_id", columnDefinition = "varchar2(50)")
    private String openId;

    /**
     * 绑定时间,yyyy-MM-dd HH:mm:ss
     */
    @Column(name = "bind_time", columnDefinition = "char(19)")
    private String bindTime;

    /**
     * 最后修改时间,yyyy-MM-dd HH:mm:ss
     */
    @Column(name = "update_time", columnDefinition = "char(19)")
    private String updateTime;

    /**
     * 绑定状态
     */
    @Column(name = "state", columnDefinition = "number(1)")
    private Integer state;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "varchar2(150)")
    private String remark;

}