package com.hlgf.sms.mapper;

import com.hlgf.sms.model.SmsSendSetting;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 公司短信设置
 *
 * @author ccr12312@163.com at 2018-11-5
 */
@Mapper
@Component
public interface SmsSendSettingMapper {

    @Insert("INSERT INTO HL_SMS_SEND_SETTING(PK_GROUP,GROUP_NAME,STATE) VALUES (#{pkGroup},#{groupName},#{state})")
    int insertSetting(SmsSendSetting setting);

    @Update("UPDATE HL_SMS_SEND_SETTING SET STATE = #{state} WHERE PK_GROUP = #{pkGroup}")
    int updateSetting(@Param("pkGroup") String pkGroup, @Param("state") int state);

    /**
     * 查找公司的设置信息
     *
     * @param pkGroup 公司
     */
    @Select("SELECT NVL(STATE,0) FROM HL_SMS_SEND_SETTING WHERE PK_GROUP = #{pkGroup} and ROWNUM = 1")
    Integer findStateByGroup(@Param("pkGroup") String pkGroup);

    @Select("select * from HL_SMS_SEND_SETTING")
    List<SmsSendSetting> getAll();
}
