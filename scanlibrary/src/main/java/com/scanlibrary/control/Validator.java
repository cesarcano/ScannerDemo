package com.scanlibrary.control;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class Validator {

    public static boolean validateForNull(Object objectToValidate) throws
            NoSuchFieldError, ClassNotFoundException, IllegalAccessException, RequiredFieldException {
        Field[] declaredFields = objectToValidate.getClass().getDeclaredFields();

        for (Field field : declaredFields) {
            Annotation annotation = field.getAnnotation(Required.class);
            if (annotation != null) {
                Required required = (Required) annotation;
                if (required.value()) {
                    field.setAccessible(true);
                    if (field.get(objectToValidate) == null) {
                        throw new RequiredFieldException(
                                objectToValidate.getClass().getName() + "." + field.getName());
                    }
                }
            }
        }

        return true;
    }

}
