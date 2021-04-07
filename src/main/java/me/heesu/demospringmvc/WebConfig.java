package me.heesu.demospringmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServlet;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //스프링 부트에서는 formatter를 Bean등록 해놓으면 해당 부분은 따로 addFormatters메서드에서 추가할 필요 없음
    /*
    @Override
    public void addFormatters(FormatterRegistry registry){
        registry.addFormatter(new PersonFormatter());
    }
     */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SampleInterceptor());
        /* 아래와 같이 특정 url패턴, 순서를 설정할 수 있음
        .addPathPatterns("/test")
        .order(-1);
         */
    }

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan(DomainObj.class.getPackageName());
        return jaxb2Marshaller;
    }
}
