package com.avans.listurmovies;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import com.avans.listurmovies.businesslogic.validation.ValidationTools;

public class LoginUnitTest {
    private final ValidationTools mValidationTools = new ValidationTools();

    @Test
    public void usernameFieldValidator_NotEmptyInputField_ReturnsFalse() {
        assertFalse(mValidationTools.isInputFieldEmpty("LoremIpsum"));
    }

    @Test
    public void usernameFieldValidator_EmptyInputField_ReturnsTrue() {
        assertTrue(mValidationTools.isInputFieldEmpty(""));
    }

    @Test
    public void passwordFieldValidator_NotEmptyInputField_ReturnsFalse() {
        assertFalse(mValidationTools.isInputFieldEmpty("LoremIpsum"));
    }

    @Test
    public void passwordFieldValidator_EmptyInputField_ReturnsTrue() {
        assertTrue(mValidationTools.isInputFieldEmpty(""));
    }
}
