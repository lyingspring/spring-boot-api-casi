package com.company.project.service;

import org.dom4j.Element;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Field;
import java.util.Iterator;

/**
 * 工具类
 * maoxj
 */
public class PublicUtil {
    /**
     * XMLElement给BEAN赋值
     * @param element
     * @param bean
     * @throws IllegalAccessException
     */
    public static void CopyXMLElementToBean(Element element, Object bean) throws IllegalAccessException {
        Iterator it = element.elementIterator();
        Class beanClass = (Class) bean.getClass();
        /*
         * 得到类中的所有属性集合
         */
        Field[] field = beanClass.getDeclaredFields();
        while (it.hasNext()) {
            Element element1 = (Element) it.next();
            for (int i = 0; i < field.length; i++) {
                Field f = field[i];
                int size = field.length;// 属性个数
                f.setAccessible(true); // 设置些属性是可以访问的
                String type = f.getType().toString();// 得到此属性的类型
                String key = f.getName();// key:得到属性名

                if (key.toUpperCase().equals(element1.getName().replace("_","").toUpperCase())) {
                    if(type.endsWith("String")) {
                        f.set(bean, element1.getStringValue());// 给属性设值
                    }
                    if(type.endsWith("Short")) {
                        f.set(bean, Short.valueOf(element1.getStringValue()));// 给属性设值
                    }
                    if(type.endsWith("Date")) {
                        continue;
                    }
                }
            }

        }

    }

}
