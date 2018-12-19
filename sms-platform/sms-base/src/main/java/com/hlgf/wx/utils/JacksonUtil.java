package com.hlgf.wx.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Pattern;

@Log4j2
@Component
public class JacksonUtil {

    private static String PHONE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    private static ObjectMapper objectMapper = new ObjectMapper();


    public static <T> T xmlToObj(String xml, Class<T> cls) {
        XmlMapper xmlMapper = prepareWrapper();
        try {
            return (T) xmlMapper.readValue(xml, cls);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T xmlToObj(XMLStreamReader reader, Class<T> cls) {
        XmlMapper xmlMapper = prepareWrapper();
        try {
            return (T) xmlMapper.readValue(reader, cls);
        } catch (IOException e) {
            return null;
        }
    }


    public static <T> T jsonToObj(String json, Class<T> cls) {

        if (StringUtils.isEmpty(json) || cls == null) {
            return null;
        }
        try {
            return cls.equals(String.class) ? (T) json : objectMapper.readValue(json, cls);
        } catch (Exception e) {
            log.warn("Parse Json to Object error", e);
            e.printStackTrace();
            return null;
        }
    }

    public static <T> String objToJson(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Parse Object to Json error", e);
            e.printStackTrace();
            return null;
        }
    }

    public static boolean validateMobile(String mobile) {
        return null != mobile && !mobile.isEmpty() && Pattern.compile(PHONE_NUMBER_REG).matcher(mobile).matches();
    }


    public static String getRandomNumber(int cnt) {
        String sources = "123456789"; // 加上一些字母，就可以生成pc站的验证码了
        Random rand = new Random();
        StringBuffer flag = new StringBuffer();
        for (int j = 0; j < cnt; j++) {
            flag.append(sources.charAt(rand.nextInt(8)) + "");
        }
        return flag.toString();
    }

    private static XmlMapper prepareWrapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setDefaultUseWrapper(false);
        //字段为null，自动忽略，不再序列化
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //XML标签名:使用骆驼命名的属性名，
        xmlMapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        //设置转换模式
        xmlMapper.enable(MapperFeature.USE_STD_BEAN_NAMING);
        return xmlMapper;
    }
}
