package com.compartamos.scannerlibrary.control;

public class RequiredFieldException extends Exception {
    private String fieldName;
    private String localisedErrorMessage;

    public RequiredFieldException(String fieldName, String localisedErrorMessage) {
        this.fieldName = fieldName;
        this.localisedErrorMessage = localisedErrorMessage;
    }

    RequiredFieldException(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "\n" + fieldName + " " + (localisedErrorMessage != null ? localisedErrorMessage : " cannot be null") ;
    }
}
