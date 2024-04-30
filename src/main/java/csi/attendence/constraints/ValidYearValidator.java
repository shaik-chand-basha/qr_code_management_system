package csi.attendence.constraints;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidYearValidator implements ConstraintValidator<ValidYear, Integer> {

	private ValidYear validYear;

	@Override
	public void initialize(ValidYear constraintAnnotation) {
		this.validYear = constraintAnnotation; // Access the parameter
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		boolean isValid = true;
		if (value == null) {
			return isValid;
		}

		Integer currentYear = Year.now().getValue();
		isValid = validateYear(value, validYear, currentYear);
		if (!isValid) {
			String messageTemplate = generateMessageTemplate();
			messageTemplate = messageTemplate+", But given "+categorizeYear(value, currentYear)+" date";
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
		}
		return isValid;

	}
	
	public String categorizeYear(int value, int currentYear) {
        if (value < currentYear) {
            return "past";
        } else if (value == currentYear) {
            return "present";
        } else {
            return "future";
        }
    }

	public boolean validateYear(int value, ValidYear validYear, int currentYear) {
		return (validYear.past() && value < currentYear) || (validYear.present() && value == currentYear)
				|| (validYear.future() && value > currentYear);
	}

	private String generateMessageTemplate() {
		StringBuilder messageBuilder = new StringBuilder("The year of joining must be ");
		List<String> constraints = new ArrayList<>();
		if (this.validYear.present()) {
			constraints.add("present");
		}
		if (this.validYear.past()) {
			constraints.add("past");
		}
		if (this.validYear.future()) {
			constraints.add("future");
		}
		messageBuilder.append(constraints.stream().collect(Collectors.joining(" or ")));
		return messageBuilder.toString();
	}
}
