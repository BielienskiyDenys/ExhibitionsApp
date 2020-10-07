package org.example.exhibitionsapp.controller;

import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Map;

public class ControllerUtil {
    static String urlAppendLocale(String url) {
        String locale = LocaleContextHolder.getLocale().toLanguageTag();
        return url+"?lang="+locale;
    }

    static void addValueToModelDependsOnLocale (Map<String, Object> model, String key, String engValue, String ukrValue) {
        if (LocaleContextHolder.getLocale().toLanguageTag() == "en_US"){
            model.put(key, engValue);
        } else {
            model.put(key, ukrValue);
        }

    }
}
