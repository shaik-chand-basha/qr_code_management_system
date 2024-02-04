package csi.attendence.constraints;
import java.time.Year;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidYearValidator implements ConstraintValidator<ValidYear,Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; 
        }
        ValidYear validYear = context.unwrap(ValidYear.class);
        boolean isValid = false;
        Integer currentYear =  Year.now().getValue();
        if(validYear.past()) {
        	isValid = value < currentYear;
        }
        return value <= zero ;
    }
}
