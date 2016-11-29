/*
 * Copyright [2016] [vincentruan]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.boot.curator.utils;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * @author vincentruan
 * @version 1.0.0
 */
public class ClassResolveUtils {

    /**
     * @param className   类名
     * @param classParams 构造参数，格式为[值:格式]
     * @return
     */
    public static Object instantiateClass(String className, String classParams) {
        Assert.hasText(className, "Class name null or empty can not be resolved!");

        Class<?> clazz = null;
        try {
            clazz = Class.forName(className.trim());
        } catch (ClassNotFoundException e) {
            throw new FatalBeanException("Failed to find class " + className.trim(), e);
        }

        Object obj = null;
        if (StringUtils.hasText(classParams)) {
            // 切割参数
            String[] params = classParams.split(",");
            Class[] paramTypes = new Class[params.length];
            Object[] paramVals = new Object[params.length];

            int i = 0;
            for (String param : params) {
                if (StringUtils.hasText(param)) {
                    // 解析参数
                    String[] resolveVals = param.split(":");
                    if (resolveVals.length == 2) {

                        String val = resolveVals[0];
                        String valType = resolveVals[1];

                        if (StringUtils.hasText(valType)) {
                            switch (valType) {
                                case "int":
                                    paramTypes[i] = int.class;
                                    paramVals[i] = Integer.parseInt(val);
                                    break;
                                case "Integer":
                                    paramTypes[i] = Integer.class;
                                    paramVals[i] = Integer.valueOf(val);
                                    break;
                                case "long":
                                    paramTypes[i] = long.class;
                                    paramVals[i] = Long.parseLong(val);
                                    break;
                                case "Long":
                                    paramTypes[i] = Long.class;
                                    paramVals[i] = Long.valueOf(val);
                                    break;
                                case "Double":
                                    paramTypes[i] = Double.class;
                                    paramVals[i] = Double.parseDouble(val);
                                    break;
                                case "Float":
                                    paramTypes[i] = Float.class;
                                    paramVals[i] = Float.parseFloat(val);
                                    break;
                                case "Boolean":
                                    paramTypes[i] = Boolean.class;
                                    paramVals[i] = Boolean.valueOf(val);
                                    break;
                                case "Short":
                                    paramTypes[i] = Short.class;
                                    paramVals[i] = Short.valueOf(val);
                                    break;
                                default:
                                    paramTypes[i] = String.class;
                                    paramVals[i] = val == null ? "" : val;
                                    break;
                            }
                        } else {
                            paramTypes[i] = String.class;
                            paramVals[i] = val == null ? "" : val;
                        }

                    } else {
                        throw new IllegalArgumentException("Failed to parse [" + param + "], the format style 'value:classType' not matched!");
                    }

                } else {
                    throw new IllegalArgumentException("Failed to parse [" + classParams + "], string between ',' can not be empty!");
                }

                i++;
            }

            Constructor ctor = null;
            try {
                ctor = clazz.getConstructor(paramTypes);

                ReflectionUtils.makeAccessible(ctor);

                obj = ctor.newInstance(paramVals);

            } catch (NoSuchMethodException e) {
                throw new BeanInstantiationException(clazz, "No such constructor for parameter types [" + classParams + "]?", e);
            } catch (IllegalAccessException e) {
                throw new BeanInstantiationException(clazz, "Is the constructor accessible?", e);
            } catch (InstantiationException e) {
                throw new BeanInstantiationException(clazz, "Is it an abstract class?", e);
            } catch (InvocationTargetException e) {
                throw new BeanInstantiationException(clazz, "Constructor threw exception", e.getTargetException());
            } catch (IllegalArgumentException e) {
                throw new BeanInstantiationException(clazz, "Illegal arguments for constructor", e);
            }

        } else {
            //不包含构造参数，使用默认构造方法
            obj = BeanUtils.instantiateClass(clazz);
        }

        return obj;

    }

}
