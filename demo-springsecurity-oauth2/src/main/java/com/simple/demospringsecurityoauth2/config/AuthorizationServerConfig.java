package com.simple.demospringsecurityoauth2.config;

import com.simple.demospringsecurityoauth2.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

/**
 * 授权服务器的配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceImpl userService;


    /**
     * 添加这个配置之后即可使用密码模式
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userService);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        /*
        这里的客户端指的是我们的应用，也是就到授权服务器这里获取授权码的应用，这里为了方便直接另其在
        内存中生成了。
         */
        clients.inMemory()
                // 客户端id
                .withClient("client")
                // 密钥
                .secret(passwordEncoder.encode("112233"))
                // 重定向地址（通过这个地址我们可以拿到授权码(地址栏)）
                .redirectUris("https://www.baidu.com")
                // 授权范围
                .scopes("all")
                /*
                授权类型
                authorization_code:授权码模式
                password: 密码模式
                 */
                .authorizedGrantTypes("authorization_code","password");
    }
}
