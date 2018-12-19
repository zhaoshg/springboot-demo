//package com.hlgf.wx.service.httpclient;
//
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.web.client.RestTemplate;
//
//import javax.annotation.Resource;
//import java.nio.charset.Charset;
//import java.util.List;
//
///**
// * RestTemplate客户端连接池配置
// *
// * @author ccr12312@163.com at 2018-11-8
// */
//@Configuration
//@EnableAspectJAutoProxy(proxyTargetClass = true)
//public class RestTemplateConfig {
//
//    @Resource
//    private CloseableHttpClient httpClient;
//
//    @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
//
//        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
//        messageConverters.add(new StringHttpMessageConverter(Charset.forName("utf-8")));
//        //微信说是返回json但是header里放的是text/plain，所以要自建一个converter，处理头部为text/plain的json
//        messageConverters.add(new WxMappingJackson2HttpMessageConverter());
//        restTemplate.setMessageConverters(messageConverters);
//
//        return restTemplate;
//    }
//
//    @Bean
//    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
//        HttpComponentsClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory();
//        rf.setHttpClient(httpClient);
//        return rf;
//    }
//}