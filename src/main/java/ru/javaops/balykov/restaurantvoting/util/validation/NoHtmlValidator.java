package ru.javaops.balykov.restaurantvoting.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

// https://github.com/JavaOPs/bootjava/blob/patched/src/main/java/ru/javaops/bootjava/util/validation/NoHtmlValidator.java
public class NoHtmlValidator implements ConstraintValidator<NoHtml, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || Jsoup.isValid(value, Safelist.none());
    }
}