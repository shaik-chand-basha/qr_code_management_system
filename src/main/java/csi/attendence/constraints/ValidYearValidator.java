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

		String messageTemplate = "";

		Integer currentYear = Year.now().getValue();
		System.out.println(currentYear);
		if (validYear.past()) {
			isValid = value < currentYear;
		} else if (validYear.present()) {
			isValid = value == currentYear;
		} else if (validYear.future()) {
			isValid = value > currentYear;
		}

		if (!isValid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(generateMessageTemplate()).addConstraintViolation();
		}
		return isValid;

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
