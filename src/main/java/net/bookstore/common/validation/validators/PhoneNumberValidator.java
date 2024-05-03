package net.bookstore.common.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.bookstore.common.config.AppConfig;
import net.bookstore.common.data.CountryCode;
import net.bookstore.common.util.PhoneNumberUtils;
import net.bookstore.common.validation.constraints.ValidPhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private AppConfig appConfig;

    @Autowired
    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return PhoneNumberUtils.validate(s, CountryCode.valueOf(appConfig.getAppCountryCode()));
    }
}
