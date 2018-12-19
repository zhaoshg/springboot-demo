//package com.purvar.springbootshirocas.config.shirocas;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import javax.servlet.Filter;
//
//import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
//import org.apache.shiro.cache.ehcache.EhCacheManager;
//import org.apache.shiro.codec.Base64;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.web.mgt.CookieRememberMeManager;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.apache.shiro.web.servlet.SimpleCookie;
//import org.pac4j.cas.client.CasClient;
//import org.pac4j.cas.config.CasConfiguration;
//import org.pac4j.core.client.Clients;
//import org.pac4j.core.config.Config;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import io.buji.pac4j.filter.CallbackFilter;
//import io.buji.pac4j.realm.Pac4jRealm;
//import io.buji.pac4j.subject.Pac4jSubjectFactory;
//
//@Configuration
//public class ShiroPac4jConfig {
//
//    // 地址为：cas地址
//    @Value("${shiro.cas}")
//    String casServerUrlPrefix;
//
//    // 地址为：验证返回后的项目地址：http://localhost:8080
//    @Value("${shiro.serverUrlPrefix}")
//    String shiroServerUrlPrefix;
//
//    // 相当于一个标志，可以随意，shiroConfig中也会用到
//    @Value("${pac4j.clientName}")
//    String clientName;
//
//    @Value("${shiro.loginUrl}")
//    String loginUrl;
//
//    @Bean
//    public Config config() {
//
//        // CAS
//        final CasConfiguration configuration = new CasConfiguration(casServerUrlPrefix + "/login", casServerUrlPrefix);
//        configuration.setAcceptAnyProxy(true);
//        CasClient casClient = new CasClient(configuration);
//        casClient.setCallbackUrl(shiroServerUrlPrefix + "/callback?client_name=" + clientName);
//        casClient.setName(clientName);
//
//        final Clients clients = new Clients(shiroServerUrlPrefix + "/callback?client_name=" + clientName, casClient);
//
//        final Config config = new Config(clients);
//        return config;
//    }
//
//    /**
//     * ShiroFilterFactoryBean 处理拦截资源文件问题。
//     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在
//     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
//     * <p>
//     * Filter Chain定义说明
//     * 1、一个URL可以配置多个Filter，使用逗号分隔
//     * 2、当设置多个过滤器时，全部验证通过，才视为通过
//     * 3、部分过滤器可指定参数，如perms，roles
//     */
//    @Bean
//    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//
//        // 必须设置 SecurityManager
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//
//        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
////        shiroFilterFactoryBean.setLoginUrl("/login");
//        shiroFilterFactoryBean.setLoginUrl(loginUrl);
//        // 登录成功后要跳转的链接
//        shiroFilterFactoryBean.setSuccessUrl("/index");
//        // 未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
//
//        //自定义拦截器
//        Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
//
//        //设置pac4j回调Filter
//        CallbackFilter callbackFilter = new CallbackFilter();
//        callbackFilter.setConfig(config());
//        callbackFilter.setDefaultUrl("/starter");
//        filtersMap.put("casFilter", callbackFilter);
//        //限制同一帐号同时在线的个数。
//        //filtersMap.put("kickout", kickoutSessionControlFilter());
//        shiroFilterFactoryBean.setFilters(filtersMap);
//
//        // 权限控制map.
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
//        filterChainDefinitionMap.put("/test", "casFilter");
//        filterChainDefinitionMap.put("/login", "anon");
//        filterChainDefinitionMap.put("/index", "anon");
//        filterChainDefinitionMap.put("/logout", "logout");
//        filterChainDefinitionMap.put("/callback", "casFilter");
//
//        // 配置不会被拦截的链接 顺序判断
//        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
//        // 从数据库获取动态的权限
//        // filterChainDefinitionMap.put("/add", "perms[权限添加]");
//        // <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
//        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
//        //logout这个拦截器是shiro已经实现好了的。
//        // 从数据库获取
//        /*List<SysPermissionInit> list = sysPermissionInitService.selectAll();
//
//        for (SysPermissionInit sysPermissionInit : list) {
//            filterChainDefinitionMap.put(sysPermissionInit.getUrl(),
//                    sysPermissionInit.getPermissionInit());
//        }*/
//
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        return shiroFilterFactoryBean;
//    }
//
//    @Bean
//    public DefaultWebSecurityManager securityManager() {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        // 设置realm.
//        securityManager.setRealm(pac4jRealm());
//
//        securityManager.setSubjectFactory(subjectFactory());
//
//        // 自定义缓存实现 使用redis
//        securityManager.setCacheManager(cacheManager());
//        // 自定义session管理 使用redis
//        //securityManager.setSessionManager(sessionManager());
//        //注入记住我管理器;
//        //securityManager.setRememberMeManager(rememberMeManager());
//        return securityManager;
//    }
//
//    @Bean
//    public EhCacheManager cacheManager() {
//        EhCacheManager ehCacheManager = new EhCacheManager();
//        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
//        return ehCacheManager;
//    }
//
//    @Bean
//    public Pac4jRealm pac4jRealm() {
//
//        //使用MD5加密
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
//        hashedCredentialsMatcher.setHashIterations(1024);
//
//        Pac4jRealm shiroRealms = new ShiroPac4jRealms();
//        shiroRealms.setCredentialsMatcher(hashedCredentialsMatcher);
//        return shiroRealms;
//    }
//
//    @Bean(name = "subjectFactory")
//    public Pac4jSubjectFactory subjectFactory() {
//        Pac4jSubjectFactory subjectFactory = new Pac4jSubjectFactory();
//        return subjectFactory;
//    }
//
//    /**
//     * cookie管理对象;记住我功能
//     *
//     * @return
//     */
//    public CookieRememberMeManager rememberMeManager() {
//        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
//        cookieRememberMeManager.setCookie(rememberMeCookie());
//        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
//        cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
//        return cookieRememberMeManager;
//    }
//
//    /**
//     * cookie对象;
//     *
//     * @return
//     */
//    public SimpleCookie rememberMeCookie() {
//        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
//        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
//        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
//        simpleCookie.setMaxAge(2592000);
//        return simpleCookie;
//    }
//}
