package org.weebook.api.exception;



public class ErrorMessages {
    /**
     * Error message for a default internal server error is status 500
     */
    public static final String DEFAULT_INTERNAL_SERVER_ERROR = "An error occurred while processing request";


    /**
     * Error message for an account not found error is status 404
     */
    public static final String ACCOUNT_NOT_FOUND_ERROR = "Account does not exist in our systems";


    /**
     * Error message for an insufficient funds error client is status 419
     */
    public static final String INSUFFICIENT_FUNDS_ERROR = "Account does not have enough funds to process this operation";
}