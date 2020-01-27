package winemall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;

/**
 * @Explain: web配置类
 * @Date: 2020/1/26
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {


    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }

    /**
     * @Explain 添加虚拟路径的映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**"). addResourceLocations("classpath:/static/");
        String uploadPath = "file:D://WineMall_Service/upload/";
        registry.addResourceHandler("/upfiles/**").addResourceLocations(uploadPath);
    }

    /**
     * @Explain 添加拦截器链      //TODO   开发接口未测试接口暂时关闭该拦截器，上线后请开启
     * @param registry
     */
    /*@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/toLogin", "/image/code", "/doLogin", "/getMsgCode", "/doForget")
                .excludePathPatterns("/static/**", "/js/**", "/css/**", "/img/**", "/plugins/**", "/vendor/**", "/error/**", "/image/**", "/upfiles/**");
    }*/
}
