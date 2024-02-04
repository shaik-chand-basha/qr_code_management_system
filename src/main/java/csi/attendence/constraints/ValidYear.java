package csi.attendence.constraints;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;

@Documented
@Constraint(validatedBy = ValidYearValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface ValidYear {
    String message() default "The year of joining must be in the present or the past";

    boolean present() default true;
    
    boolean past() default false;
    
    boolean future() default false;
    
    
    
}
