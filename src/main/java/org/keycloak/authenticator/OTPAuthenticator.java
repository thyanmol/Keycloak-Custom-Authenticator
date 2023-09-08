package org.keycloak.authenticator;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.UserModel;


import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OTPAuthenticator implements Authenticator{

    public static String MOBILE_NUMBER_LOGIN_FORM = "mobile-number-login-form.ftl";
    public static String MOBILE_OTP_FORM = "mobile-otp-form.ftl";
    public static String USER_NOT_FOUND_ERROR = "User not found";
    public static String INVALID_OTP_ERROR = "Invalid OTP";
    public static String INVALID_MOBILE_NUMBER_ERROR = "Invalid Mobile Number";
    public static String MOBILE_NUMBER_VALIDATED_MESSAGE = "Mobile Number Validated";
    public static String AUTHENTICATION_SUCCESSFUL_MESSAGE = "Authentication Successful";

    Logger logger = Logger.getLogger(OTPAuthenticator.class.getName());
    UserModel userModel = null;
    String mobileNumber = null;


    // This is the method where the control starts when your custom login page loads
    @Override
    public void authenticate(AuthenticationFlowContext authenticationFlowContext) {
        authenticationFlowContext.challenge(authenticationFlowContext.form().createForm(MOBILE_NUMBER_LOGIN_FORM));
    }

    // This validates if the user exists with the same username as mobile number
    protected boolean validateUser(AuthenticationFlowContext context, String mobileNumber) {
        userModel = context.getSession().users().getUserByUsername(context.getRealm(), mobileNumber);
        return userModel != null;
    }

    // This validates if the entered mobile number is valid
    public String validateMobileNumber(String mobileNumber){
        Pattern VALID_MOBILE_REGEX = Pattern.compile("^[0-9]{10}$");
        Matcher matcher = VALID_MOBILE_REGEX.matcher(mobileNumber);

        if(matcher.find())
            return mobileNumber;
        else
            return null;
    }

    // This validates the OTP. Here the OTP is hardcoded, you can use any service to generate and validate OTP
    public boolean validateOTP(String otp){
        if(otp.equals("1234"))
            return true;

        return false;
    }

    // When you click submit button on your login page, the control goes on this method
    @Override
    public void action(AuthenticationFlowContext context) {

        String otp = "";

        if (context.getHttpRequest().getDecodedFormParameters().get("mobileNumber") != null) {
            mobileNumber = validateMobileNumber(context.getHttpRequest().getDecodedFormParameters().get("mobileNumber").get(0));

            if (mobileNumber == null) {
                context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, context.form()
                        .setError(INVALID_MOBILE_NUMBER_ERROR)
                        .createForm(MOBILE_NUMBER_LOGIN_FORM));

                logger.warning(INVALID_MOBILE_NUMBER_ERROR);
            } else if (!validateUser(context, mobileNumber)) {

                logger.info(MOBILE_NUMBER_VALIDATED_MESSAGE);

                context.failureChallenge(AuthenticationFlowError.INVALID_USER, context.form()
                        .setError(USER_NOT_FOUND_ERROR)
                        .createForm(MOBILE_NUMBER_LOGIN_FORM));

                logger.warning(USER_NOT_FOUND_ERROR);
            }

        }

        else if(context.getHttpRequest().getDecodedFormParameters().get("otp") != null) {
            otp = context.getHttpRequest().getDecodedFormParameters().get("otp").get(0);

            if (!validateOTP(otp)) {
                context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, context.form()
                        .setError(INVALID_OTP_ERROR)
                        .createForm(MOBILE_OTP_FORM));

                logger.warning(INVALID_OTP_ERROR);
            }
            else {
                logger.info(AUTHENTICATION_SUCCESSFUL_MESSAGE);
                context.setUser(userModel);
                context.success();
            }

        }
    }

    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(org.keycloak.models.KeycloakSession keycloakSession, org.keycloak.models.RealmModel realmModel, org.keycloak.models.UserModel userModel) {
        return false;
    }

    @Override
    public void setRequiredActions(org.keycloak.models.KeycloakSession keycloakSession, org.keycloak.models.RealmModel realmModel, org.keycloak.models.UserModel userModel) {

    }

    @Override
    public void close() {

    }

}
