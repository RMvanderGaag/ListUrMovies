package com.avans.listurmovies;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import com.avans.listurmovies.businesslogic.validation.ValidationTools;

/**
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class LoginUnitTest {
    private final ValidationTools validationTools = new ValidationTools();

    @Test
    public void usernameFieldValidator_NotEmptyInputField_ReturnsFalse() {
        assertFalse(validationTools.isInputFieldEmpty("LoremIpsum"));
    }

    @Test
    public void usernameFieldValidator_EmptyInputField_ReturnsTrue() {
        assertTrue(validationTools.isInputFieldEmpty(""));
    }

    @Test
    public void passwordFieldValidator_NotEmptyInputField_ReturnsFalse() {
        assertFalse(validationTools.isInputFieldEmpty("LoremIpsum"));
    }

    @Test
    public void passwordFieldValidator_EmptyInputField_ReturnsTrue() {
        assertTrue(validationTools.isInputFieldEmpty(""));
    }
}
