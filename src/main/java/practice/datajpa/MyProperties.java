package practice.datajpa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class MyProperties {

    @Bean("testBean")
    public Map getMap(@Value("#{${test.name.other}}") Map<String,String> myMap) {
        return myMap;
    }
}
