package com.librarymanagementsystem.util;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component("customKeyGenerator")
public class CustomKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {

        //BookService::getBookDetails::978-3-16-148410-0,Murad Sharifzada

        StringBuilder key = new StringBuilder();

        // Add class name
        key.append(target.getClass().getSimpleName()).append("::");

        // Add method name
        key.append(method.getName()).append("::");

        // Add parameters
        for (Object param : params) {
            if (param != null) {
                key.append(param.toString()).append(",");
            }
        }

        // Remove trailing comma
        if (!key.isEmpty() && key.charAt(key.length() - 1) == ',') {
            key.deleteCharAt(key.length() - 1);
        }

        return key.toString();
    }
}
