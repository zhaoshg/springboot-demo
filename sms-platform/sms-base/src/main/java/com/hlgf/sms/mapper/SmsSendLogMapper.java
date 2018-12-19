package com.hlgf.sms.mapper;

import com.hlgf.sms.model.SmsSendLog;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 发送短信日志
 *
 * @author ccr12312@163.com at 2018-10-29
 */
@Mapper
@Component
public interface SmsSendLogMapper {

    @Insert("INSERT INTO " +
            "HL_SMS_SEND_LOG(ID,RECEIVER_NAME,RECEIVER_NUMBER,PK_RECEIVER,SEND_TIME,SEND_STATE,SEND_COUNT,CONTENT,WORD_COUNT,RELATED_ORDER,RELATED_ID,PK_GROUP,GROUP_NAME,SMS_TYPE) " +
            "VALUES(SMS_LOG_SEQUENCE.NEXTVAL,#{receiverName}, #{receiverNumber},#{pkReceiver}, #{sendTime}, #{sendState}, #{sendCnt}, #{content}, #{wordCount}, #{relatedOrder, jdbcType=VARCHAR}, #{relatedId},#{pkGroup},#{groupName},#{smsType})")
    int insertLog(SmsSendLog log);

    @Insert({"<script>    INSERT INTO HL_SMS_SEND_LOG(ID,RECEIVER_NAME,RECEIVER_NUMBER,PK_RECEIVER,SEND_TIME,SEND_STATE,SEND_COUNT,CONTENT,WORD_COUNT,RELATED_ORDER,RELATED_ID,PK_GROUP,GROUP_NAME,SMS_TYPE)  \n" +
            "    SELECT SMS_LOG_SEQUENCE.NEXTVAL,a.* FROM (\n" +
            "      <foreach collection='logList' item='item' separator='union all'>" +
            "        SELECT " +
            "        #{item.receiverName}, #{item.receiverNumber},#{item.pkReceiver}, #{item.sendTime}, #{item.sendState}, #{item.sendCnt}, #{item.content}, #{item.wordCount}, #{item.relatedOrder, jdbcType=VARCHAR},#{item.relatedId},#{item.pkGroup},#{item.groupName},#{item.smsType} " +
            "        FROM dual\n" +
            "      </foreach>" +
            "    ) a</script>"})
    @Options(useGeneratedKeys = false)
    int batchInsertLog(@Param("logList") List<SmsSendLog> list);

    @Select("select * from HL_SMS_SEND_LOG where RELATED_ID = #{relatedId} and ROWNUM = 1")
    SmsSendLog getLogByRelatedId(@Param("relatedId") String relatedId);

    /**
     * 查找最近一次插入记录的时间
     *
     * @return 最近一次插入记录的时间
     */
    @Select("SELECT SEND_TIME FROM (SELECT SEND_TIME FROM HL_SMS_SEND_LOG WHERE SMS_TYPE = #{smsType} AND SEND_STATE = 'Y' ORDER BY ID DESC) t  where ROWNUM = 1 ")
    String findLastInsertTime(@Param("smsType") String smsType);

    /**
     * 从 sendTime 开始的receiverNumber和relatedId的发送次数
     *
     * @return 发送次数
     */
    @Select("<script> " +
            "SELECT * FROM HL_SMS_SEND_LOG WHERE " +
            "<if test='relatedIdList != null and relatedIdList.size() > 0'> " +
            " RELATED_ID in " +
            "<foreach item='relatedId' collection='relatedIdList' open='(' separator=',' close=')'> " +
            "#{relatedId} " +
            "</foreach> and " +
            "</if> " +
            "<if test='receiverNumber != null'> RECEIVER_NUMBER = #{receiverNumber}  and</if> " +
            "<if test='smsType != null'> SMS_TYPE = #{smsType} and</if> " +
            "<if test='sendTime != null'> SEND_TIME > #{sendTime} and</if> " +
            " SEND_STATE = 'Y' " +
            "</script>")
    List<SmsSendLog> findList(@Param("relatedIdList") List<String> relatedIdList, @Param("receiverNumber") String receiverNumber, @Param("smsType") String smsType, @Param("sendTime") String sendTime);
}
