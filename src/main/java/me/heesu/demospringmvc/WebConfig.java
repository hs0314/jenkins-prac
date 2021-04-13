package me.heesu.demospringmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServlet;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //스프링 부트에서는 formatter를 Bean등록 해놓으면 해당 부분은 따로 addFormatters메서드에서 추가할 필요 없음
    /*
    @Override
    public void addFormatters(FormatterRegistry registry){
        registry.addFormatter(new PersonFormatter());
    }
     */

    /**
     * @MatrixVariable 을 사용하기 위해서 요청url에서 세미콜론을 자동으로 지우지 않도록 처리
     * @param configurer
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer){
        UrlPathHelper helper = new UrlPathHelper();
        helper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(helper);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SampleInterceptor());
        /* 아래와 같이 특정 url패턴, 순서를 설정할 수 있음
        .addPathPatterns("/test")
        .order(-1);
         */
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
         정적 리소스에 대한 리소스핸들러 셋팅
         스프링부트는 기본적으로 정적리소스 핸들러와 캐싱 제공
         */
        registry.addResourceHandler("/mobile/**")
                .addResourceLocations("classpath:/mobile/")
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
    }

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller(){
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();

        //packageToScan을 통해서 @XmlRootElement 어노테이션을 통해서 실제 xml변환이 필요한 객체를 명시
        jaxb2Marshaller.setPackagesToScan(DomainObj.class.getPackageName());
        jaxb2Marshaller.setPackagesToScan(Person.class.getPackageName());
        return jaxb2Marshaller;
    }
}
