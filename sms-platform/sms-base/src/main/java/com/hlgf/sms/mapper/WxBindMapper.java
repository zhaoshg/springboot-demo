package com.hlgf.sms.mapper;

import com.hlgf.sms.model.WxBindEntity;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 微信绑定信息
 *
 */
@Mapper
@Component
public interface WxBindMapper {

    @Insert("INSERT INTO " +
            "HL_WX_BIND(ID,NICK_NAME,PHONE,OPEN_ID,BIND_TIME,UPDATE_TIME,STATE,REMARK) " +
            "VALUES(WX_BIND_SEQUENCE.NEXTVAL,#{bind.nickName}, #{bind.phone},#{bind.openId,jdbcType=VARCHAR}, #{bind.bindTime}, #{bind.updateTime}, #{bind.state}, #{bind.remark})")
    int insertLog(@Param("bind") WxBindEntity bind);

    @Select("select * from HL_WX_BIND where STATE = 1")
    List<WxBindEntity> getAll();

    @Select("select * from HL_WX_BIND where PHONE = #{phone} and ROWNUM = 1")
    WxBindEntity getBindByPhone(@Param("phone") String phone);

    @Update("update HL_WX_BIND set NICK_NAME = #{bind.nickName},OPEN_ID='${bind.openId}',UPDATE_TIME=#{bind.updateTime},STATE=#{bind.state},REMARK=#{bind.remark} where phone = #{bind.phone}")
    int updateBindInfo(@Param("bind") WxBindEntity bind);
}
