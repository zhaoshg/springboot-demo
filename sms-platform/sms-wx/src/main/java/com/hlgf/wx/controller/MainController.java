package com.hlgf.wx.controller;


import com.hlgf.wx.model.Message;
import com.hlgf.wx.utils.JacksonUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Log4j2
@Controller
@RequestMapping("/wx")
public class MainController {

    @Value("${wx.token}")
    private String token;

    /**
     * 接入公众号时使用，用来验证消息的确来自微信服务器
     *
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    String doGet(HttpServletRequest request) {
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        log.info("given signature is {},echostr ={}", signature, echostr);
        if (checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return "success";
    }

    /**
     * 接收公众号发来的消息
     *
     * @param request
     * @return
     * @throws IOException
     * @throws XMLStreamException
     */
    @RequestMapping(method = RequestMethod.POST, produces = "text/xml;charset=UTF-8")
    public @ResponseBody
    String doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, XMLStreamException {
        ServletInputStream is = (ServletInputStream) request.getInputStream();
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(is);
        Message msg = JacksonUtil.xmlToObj(reader, Message.class);
        log.info("收到微信消息：{}", msg);
        return "";
    }


    /**
     * 方法名：checkSignature</br>
     * 详述：验证签名</br>
     * 开发人员：
     * 创建时间：
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     * @throws
     */
    public boolean checkSignature(String signature, String timestamp, String nonce) {
        // 1.将token、timestamp、nonce三个参数进行字典序排序
        String[] arr = new String[]{token, timestamp, nonce};
        Arrays.sort(arr);

        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        log.info("calculated signature is {}", tmpStr);
        content = null;
        // 3.将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }


    private String byteToStr(byte[] byteArray) {
        StringBuilder strDigest = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            strDigest.append(byteToHexStr(byteArray[i]));
        }
        return strDigest.toString();
    }

    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }
}