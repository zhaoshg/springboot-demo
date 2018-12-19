//package com.purvar.springbootshirocas.config.shirocas;
//
//import java.util.HashSet;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Set;
//
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.AuthenticationException;
//import org.apache.shiro.authc.AuthenticationInfo;
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.authc.LockedAccountException;
//import org.apache.shiro.authc.SimpleAuthenticationInfo;
//import org.apache.shiro.authc.UnknownAccountException;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.session.Session;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.apache.shiro.util.ByteSource;
//import org.pac4j.core.profile.CommonProfile;
//
//import io.buji.pac4j.realm.Pac4jRealm;
//import io.buji.pac4j.subject.Pac4jPrincipal;
//import io.buji.pac4j.token.Pac4jToken;
//
///**
// * 授权需要继承 AuthorizingRealm 类，并实现 doGetAuthenticationInfo 方法
// *
// * @author Administrator
// */
//public class ShiroPac4jRealms extends Pac4jRealm {
//
//    // 认证
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        System.out.println("ShiroRealms 开始认证！");
//
//
//        // 1. 把 AuthenticationToken 转换为 Pac4jToken
//        final Pac4jToken token = (Pac4jToken) authenticationToken;
//
//
//        // 2. 从 UsernamePasswordToken 中来获取 username
//        final LinkedHashMap<String, CommonProfile> profiles = (LinkedHashMap<String, CommonProfile>) token.getProfiles();
//        final Pac4jPrincipal principal = new Pac4jPrincipal((List<CommonProfile>) profiles);
//        String loginName = principal.getProfile().getId();
//
//        Session session = SecurityUtils.getSubject().getSession();
//        session.setAttribute("userSessionId", loginName);
//
//        // 3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
//        System.out.println("从数据库中获取 loginName: " + loginName + " 所对应的用户信息.");
//
//        // 4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
//        if ("unknown".equals(loginName)) {
//            throw new UnknownAccountException("用户不存在!");
//        }
//
//        // 5. 根据用户信息的情况, 决定是否需要抛出其他的 AuthenticationException 异常.
//        if ("monster".equals(loginName)) {
//            throw new LockedAccountException("用户被锁定");
//        }
//
//        // 6. 根据用户的情况, 来构建 AuthenticationInfo 对象并返回. 通常使用的实现类为:
//        // SimpleAuthenticationInfo
//
//        // 以下信息是从数据库中获取的.
//        // 1). principal: 认证的实体信息. 可以是 username, 也可以是数据表对应的用户的实体类对象.
//        // 2). credentials: 密码.
//        Object credentials = null;
//        if ("admin".equals(loginName)) {
//            credentials = "038bdaf98f2037b31f1e75b5b4c9b26e";
//        } else if ("user".equals(loginName)) {
//            credentials = "098d2c478e9c11555ce2823231e02ec1";
//        }
//        // 3). realmName: 当前 realm 对象的 name. 调用父类的 getName() 方法即可
//        String realmName = getName();
//        // 4). ByteSource credentialsSalt 盐值，相同的密码，加密之后密文是一样的，加入盐值使每个人的密码都不一样
//        ByteSource salt = ByteSource.Util.bytes(loginName);
//
//        // SimpleAuthenticationInfo info = new
//        // SimpleAuthenticationInfo(principal, credentials, realmName);
//        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, salt, realmName);
//        //SimpleAuthenticationInfo info1 = new SimpleAuthenticationInfo(principal, profiles.hashCode(), realmName);
//
//        return info;
//    }
//
//    // 授权
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        // 1. 从 PrincipalCollection 中来获取登录用户的信息
//        Object principal = principals.getPrimaryPrincipal();
//
//        // 2. 利用登录的用户的信息来用户当前用户的角色或权限(可能需要查询数据库)
//        Set<String> roles = new HashSet<>();
//        roles.add("user");
//        if ("admin".equals(principal)) {
//            roles.add("admin");
//        }
//
//        // 3. 创建 SimpleAuthorizationInfo, 并设置其 reles 属性.
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
//
//        // 4. 返回 SimpleAuthorizationInfo 对象.
//        return info;
//    }
//
//}
