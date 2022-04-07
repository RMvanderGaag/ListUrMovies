package com.avans.listurmovies;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.avans.listurmovies.businesslogic.validation.ValidationTools;

import org.junit.Test;

public class MovieListAddUnitTest {
    private final ValidationTools mValidationTools = new ValidationTools();

    @Test
    public void nameFieldValidator_NotEmptyInputField_ReturnsFalse() {
        assertFalse(mValidationTools.isInputFieldEmpty("LoremIpsum"));
    }

    @Test
    public void nameFieldValidator_EmptyInputField_ReturnsTrue() {
        assertTrue(mValidationTools.isInputFieldEmpty(""));
    }

    @Test
    public void descriptionFieldValidator_NotEmptyInputField_ReturnsFalse() {
        assertFalse(mValidationTools.isInputFieldEmpty("LoremIpsumDolorSitAmet"));
    }

    @Test
    public void descriptionFieldValidator_EmptyInputField_ReturnsTrue() {
        assertTrue(mValidationTools.isInputFieldEmpty(""));
    }
}
