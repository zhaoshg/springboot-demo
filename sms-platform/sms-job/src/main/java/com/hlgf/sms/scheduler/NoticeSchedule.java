package com.hlgf.sms.scheduler;

import com.hlgf.sms.Constants;
import com.hlgf.sms.model.SmsSendLog;
import com.hlgf.sms.model.WorkListEntity;
import com.hlgf.sms.service.MsgSender;
import com.hlgf.sms.service.SmsSendLogService;
import com.hlgf.sms.service.WorkListService;
import com.hlgf.wx.WxConstants;
import com.hlgf.wx.message.TemplateSender;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

@Log4j2
@Data
@Getter
@Setter
@Component
public class NoticeSchedule {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //当查询数据过多时，分批处理，每批处理个数
    private static final int excutorSize = 500;

    private static String PHONE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    private String template;

    private String smsType = Constants.SmsType.SMS_TYPE_WORK_LIST_REMIND;

    @Autowired
    private MsgSender msgSender;

    private TemplateSender templateSender;

    @Autowired
    private WorkListService workListService;

    @Autowired
    private SmsSendLogService logService;

    public void sendSMS() {
        long nano = System.currentTimeMillis();
        log.info("========{} job start scheduling at {}", this.getClass().getSimpleName(), dateFormat.format(new Date()));
        String lastSendTime = logService.findLastInsertTime(smsType);
        List<WorkListEntity> workList = workListService.findWorkListUnhandledRecently(lastSendTime, smsType);
        if (workList == null)
            workList = new ArrayList<>();
        long time1 = System.currentTimeMillis();
        log.info("{} job loaded List after {},loaded {} records ,cost {} MS.", this.getClass().getSimpleName(), lastSendTime, workList.size(), (time1 - nano));
        int size = workList.size();
        if (size > 0) {
            if (size > excutorSize) {
                int part = size / excutorSize;//分批数
                log.info("{} job total Data : {} ，Split into {} executions ", this.getClass().getSimpleName(), size, part);
                for (int i = 0; i < part; i++) {
                    List listPage = workList.subList(0, excutorSize);
                    long start = System.currentTimeMillis();
                    log.info("{} job start the {} execution", this.getClass().getSimpleName(), (i + 1));
                    executor(listPage);
                    long end = System.currentTimeMillis();
                    log.info("{} job end the {} execution , cost {} MS", this.getClass().getSimpleName(), (i + 1), (end - start));
                    workList.subList(0, excutorSize).clear();
                }
            }
            if (!workList.isEmpty()) {
                long start = System.currentTimeMillis();
                log.info("{} job start the last execution", this.getClass().getSimpleName());
                executor(workList);
                long end = System.currentTimeMillis();
                log.info("{} job end the last execution , cost {} MS", this.getClass().getSimpleName(), (end - start));
            }
        }
        log.info("========{} job done scheduling at {} with {}ms", this.getClass().getSimpleName(), dateFormat.format(new Date()), System.currentTimeMillis() - nano);
    }

    /**
     * 处理获取的数据
     *
     * @param workList
     */
    private void executor(List<WorkListEntity> workList) {
        long time0 = System.currentTimeMillis();
        //批量查询smsSendLog,判断是否已经发送三次
        Map<String, Integer> cntMap = new HashMap();
        List relatedList = new ArrayList();
        workList.forEach(x -> {
            relatedList.add(x.getPkMessage());
        });
        if (!relatedList.isEmpty()) {
            List<SmsSendLog> logList = logService.findList(null, relatedList, smsType, null);

            logList.forEach(x -> {
                if (cntMap.containsKey(x.getRelatedId())) {
                    int cnt = cntMap.get(x.getRelatedId());
                    cntMap.put(x.getRelatedId(), cnt + 1);
                } else {
                    cntMap.put(x.getRelatedId(), 1);
                }
            });
        }
        Map<String, SmsSendLog> smsMap = new HashMap<>();
        //用来存储 需要微信发送的实体
        List<WorkListEntity> wxList = new ArrayList<>();
        workList.stream().filter(x -> {
            if (!validateMobile(x.getMobile())) {
                log.error("{} invalid mobile number {},receiver name {},ignored {}.", this.getClass().getSimpleName(), x.getMobile(), x.getReceiverName(), x.getPkMessage());
                return false;
            } else {
                if (smsType.equals(Constants.SmsType.SMS_TYPE_WORK_LIST_REMIND) && cntMap.containsKey(x.getPkMessage()) && cntMap.get(x.getPkMessage()) > 2) {
                    log.error("{} target mobile {}, related pk {} has already send 3 times.", this.getClass().getSimpleName(), x.getMobile(), x.getPkMessage());
                    return false;
                } else if (cntMap.containsKey(x.getPkMessage())) {
                    log.error("{} target mobile {}, related pk {} has already send 1 times.", this.getClass().getSimpleName(), x.getMobile(), x.getPkMessage());
                    return false;
                } else if (WxConstants.getBindInfo().containsKey(x.getMobile())) {
                    x.setOpenId(WxConstants.getBindInfo().get(x.getMobile()));
                    wxList.add(x);
                    return false;
                }
                return true;
            }
        }).forEach(x -> {
            String content = String.format(template, x.getSubject());
            SmsSendLog sms = x.toSmsSendLog();
            sms.setContent(content);
            sms.setSmsType(smsType);
            sms.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            sms.setWordCount(sms.getContent().length());
            sms.setSendCnt(1);
            smsMap.put(x.getPkMessage(), sms);
            //FIXME for test
            wxList.add(x);
        });

        long time1 = System.currentTimeMillis();
        log.info(" {}  job's executor prepared for Sms end , cost {} ms", this.getClass().getSimpleName(), (time1 - time0));
        log.info(" {}  job's executor send Sms start ", this.getClass().getSimpleName());
        msgSender.betchSend(smsMap);

        //这里调用微信发送接口
        for (WorkListEntity entity : wxList) {
            templateSender.sendWxMsg(entity);
        }

        smsMap.entrySet().forEach(x -> {
            SmsSendLog sms = x.getValue();
            log.info("{} job's executor send Sms {} , receiver is {} ,mobile is {}, content is {}, ", this.getClass().getSimpleName(), sms.getSendState().equals("Y") ? "success" : "failure", sms.getReceiverName(), sms.getReceiverNumber(), sms.getContent());
        });
        log.info(" {}  job's executor send Sms end , cost {} ms", this.getClass().getSimpleName(), (System.currentTimeMillis() - time1));
    }

    private boolean validateMobile(String mobile) {
        return null != mobile && !mobile.isEmpty() && Pattern.compile(PHONE_NUMBER_REG).matcher(mobile).matches();
    }
}
