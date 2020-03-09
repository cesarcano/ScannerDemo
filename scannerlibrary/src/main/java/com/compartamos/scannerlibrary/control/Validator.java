package com.compartamos.scannerlibrary.control;

import com.google.gson.JsonParser;

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

    public static boolean jsonValidator(String jsonAsString) {
        if (new JsonParser().parse(jsonAsString).isJsonNull()) return false;
        if (new JsonParser().parse(jsonAsString).isJsonArray()) return jsonAsString.length() > 0;
        return new JsonParser().parse(jsonAsString).isJsonObject();
    }
}
