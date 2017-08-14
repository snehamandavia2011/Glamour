package entity;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import utility.Helper;
import utility.Logger;

/**
 * Created by SAI on 8/4/2017.
 */

public class User {
    private String firstName, lastName, emailId, password, mobileNumber, userType, companyName, companyPhone, companyHouseNo, companyStreet, companyLandmark, companyPostCode, companyCity, companyState, companyCountry, isOwner, isSalesMan, token;

    public User(String emailId, String password) {
        this.emailId = emailId;
        this.password = password;
    }

    public User(String companyHouseNo, String companyStreet, String companyLandmark, String companyPostCode, String companyCity, String companyState, String companyCountry) {
        this.companyHouseNo = companyHouseNo;
        this.companyStreet = companyStreet;
        this.companyLandmark = companyLandmark;
        this.companyPostCode = companyPostCode;
        this.companyCity = companyCity;
        this.companyState = companyState;
        this.companyCountry = companyCountry;
    }

    public User(String firstName, String lastName, String mobileNumber, String userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.userType = userType;
    }

    public User(String emailId, String companyName, String companyPhone, String isOwner, String isSalesMan) {
        this.emailId = emailId;
        this.companyName = companyName;
        this.companyPhone = companyPhone;
        this.isOwner = isOwner;
        this.isSalesMan = isSalesMan;
    }

    public User() {
    }

    public class Fields {
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String EMAILID = "emailId";
        public static final String PASSWORD = "password";
        public static final String MOBILE_NUMBER = "mobile_number";
        public static final String USER_TYPE = "user_type";
        public static final String COMPANY_NAME = "company_name";
        public static final String COMPANY_PHONE = "company_phone";
        public static final String COMPANY_HOUSE_NO = "company_house_no";
        public static final String COMPANY_STREET = "company_street";
        public static final String COMPANY_LANDMARK = "company_landmark";
        public static final String COMPANY_POST_CODE = "company_post_code";
        public static final String COMPANY_CITY = "company_city";
        public static final String COMPANY_STATE = "company_state";
        public static final String COMPANY_COUNTRY = "company_country";
        public static final String IS_OWNER = "is_owner";
        public static final String IS_SALES_MAN = "is_sales_man";
        public static final String TOKEN = "token";
    }

    public static void clearCache(Context c) {
        Helper.clearPreference(c, Fields.FIRST_NAME);
        Helper.clearPreference(c, Fields.LAST_NAME);
        Helper.clearPreference(c, Fields.EMAILID);
        Helper.clearPreference(c, Fields.PASSWORD);
        Helper.clearPreference(c, Fields.MOBILE_NUMBER);
        Helper.clearPreference(c, Fields.USER_TYPE);
        Helper.clearPreference(c, Fields.COMPANY_NAME);
        Helper.clearPreference(c, Fields.COMPANY_PHONE);
        Helper.clearPreference(c, Fields.COMPANY_HOUSE_NO);
        Helper.clearPreference(c, Fields.COMPANY_STREET);
        Helper.clearPreference(c, Fields.COMPANY_LANDMARK);
        Helper.clearPreference(c, Fields.COMPANY_POST_CODE);
        Helper.clearPreference(c, Fields.COMPANY_CITY);
        Helper.clearPreference(c, Fields.COMPANY_STATE);
        Helper.clearPreference(c, Fields.COMPANY_COUNTRY);
        Helper.clearPreference(c, Fields.IS_OWNER);
        Helper.clearPreference(c, Fields.IS_SALES_MAN);
        Helper.clearPreference(c, Fields.TOKEN);
    }

    public void saveFiledsToPreferences(Context c) {
        Helper.setStringPreference(c, Fields.FIRST_NAME, this.getFirstName());
        Helper.setStringPreference(c, Fields.LAST_NAME, this.getLastName());
        Helper.setStringPreference(c, Fields.EMAILID, this.getEmailId());
        Helper.setStringPreference(c, Fields.PASSWORD, this.getPassword());
        Helper.setStringPreference(c, Fields.MOBILE_NUMBER, this.getMobileNumber());
        Helper.setStringPreference(c, Fields.USER_TYPE, this.getUserType());
        Helper.setStringPreference(c, Fields.COMPANY_NAME, this.getCompanyName());
        Helper.setStringPreference(c, Fields.COMPANY_PHONE, this.getCompanyPhone());
        Helper.setStringPreference(c, Fields.COMPANY_HOUSE_NO, this.getCompanyHouseNo());
        Helper.setStringPreference(c, Fields.COMPANY_STREET, this.getCompanyStreet());
        Helper.setStringPreference(c, Fields.COMPANY_LANDMARK, this.getCompanyLandmark());
        Helper.setStringPreference(c, Fields.COMPANY_POST_CODE, this.getCompanyPostCode());
        Helper.setStringPreference(c, Fields.COMPANY_CITY, this.getCompanyCity());
        Helper.setStringPreference(c, Fields.COMPANY_STATE, this.getCompanyState());
        Helper.setStringPreference(c, Fields.COMPANY_COUNTRY, this.getCompanyCountry());
        Helper.setStringPreference(c, Fields.IS_OWNER, this.getIsOwner());
        Helper.setStringPreference(c, Fields.IS_SALES_MAN, this.getIsSalesMan());
        Helper.setStringPreference(c, Fields.TOKEN, this.getToken());
    }

    public void saveCompanyAddressToPreference(Context c) {
        Helper.setStringPreference(c, Fields.COMPANY_HOUSE_NO, this.getCompanyHouseNo());
        Helper.setStringPreference(c, Fields.COMPANY_STREET, this.getCompanyStreet());
        Helper.setStringPreference(c, Fields.COMPANY_LANDMARK, this.getCompanyLandmark());
        Helper.setStringPreference(c, Fields.COMPANY_POST_CODE, this.getCompanyPostCode());
        Helper.setStringPreference(c, Fields.COMPANY_CITY, this.getCompanyCity());
        Helper.setStringPreference(c, Fields.COMPANY_STATE, this.getCompanyState());
        Helper.setStringPreference(c, Fields.COMPANY_COUNTRY, this.getCompanyCountry());
    }

    public void savePersonalDetailToPreference(Context c) {
        Helper.setStringPreference(c, Fields.FIRST_NAME, this.getFirstName());
        Helper.setStringPreference(c, Fields.LAST_NAME, this.getLastName());
        Helper.setStringPreference(c, Fields.MOBILE_NUMBER, this.getMobileNumber());
        Helper.setStringPreference(c, Fields.USER_TYPE, this.getUserType());
    }

    public void saveCompanyDetailToPreference(Context c) {
        Helper.setStringPreference(c, Fields.COMPANY_NAME, this.getCompanyName());
        Helper.setStringPreference(c, Fields.COMPANY_PHONE, this.getCompanyPhone());
        Helper.setStringPreference(c, Fields.IS_OWNER, this.getIsOwner());
        Helper.setStringPreference(c, Fields.IS_SALES_MAN, this.getIsSalesMan());
    }

    public void savePasswordDetailToPreference(Context c){
        Helper.setStringPreference(c, Fields.PASSWORD, this.getPassword());
    }

    public void parseJSON(JSONObject objJSON) {
        try {
            this.setPassword(objJSON.getString("password").equals("null") ? "" : objJSON.getString("password"));
            this.setFirstName(objJSON.getString("first_name").equals("null") ? "" : objJSON.getString("first_name"));
            this.setLastName(objJSON.getString("last_name").equals("null") ? "" : objJSON.getString("last_name"));
            this.setMobileNumber(objJSON.getString("contact_no").equals("null") ? "" : objJSON.getString("contact_no"));
            this.setEmailId(objJSON.getString("email").equals("null") ? "" : objJSON.getString("email"));
            this.setUserType(objJSON.getString("user_type").equals("null") ? "" : objJSON.getString("user_type"));
            this.setCompanyName(objJSON.getString("company_name").equals("null") ? "" : objJSON.getString("company_name"));
            this.setCompanyHouseNo(objJSON.getString("company_house_no").equals("null") ? "" : objJSON.getString("company_house_no"));
            this.setCompanyStreet(objJSON.getString("company_street").equals("null") ? "" : objJSON.getString("company_street"));
            this.setCompanyLandmark(objJSON.getString("company_landmark").equals("null") ? "" : objJSON.getString("company_landmark"));
            this.setCompanyPostCode(objJSON.getString("company_postcode").equals("null") ? "" : objJSON.getString("company_postcode"));
            this.setCompanyCity(objJSON.getString("company_city").equals("null") ? "" : objJSON.getString("company_city"));
            this.setCompanyState(objJSON.getString("company_state").equals("null") ? "" : objJSON.getString("company_state"));
            this.setCompanyPhone(objJSON.getString("company_phone").equals("null") ? "" : objJSON.getString("company_phone"));
            this.setIsOwner(objJSON.getString("is_owner").equals("null") ? "" : objJSON.getString("is_owner"));
            this.setIsSalesMan(objJSON.getString("is_salesmane").equals("null") ? "" : objJSON.getString("is_salesmane"));
            this.setToken(objJSON.getString("token").equals("null") ? "" : objJSON.getString("token"));
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyHouseNo() {
        return companyHouseNo;
    }

    public void setCompanyHouseNo(String companyHouseNo) {
        this.companyHouseNo = companyHouseNo;
    }

    public String getCompanyStreet() {
        return companyStreet;
    }

    public void setCompanyStreet(String companyStreet) {
        this.companyStreet = companyStreet;
    }

    public String getCompanyLandmark() {
        return companyLandmark;
    }

    public void setCompanyLandmark(String companyLandmark) {
        this.companyLandmark = companyLandmark;
    }

    public String getCompanyPostCode() {
        return companyPostCode;
    }

    public void setCompanyPostCode(String companyPostCode) {
        this.companyPostCode = companyPostCode;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyState() {
        return companyState;
    }

    public void setCompanyState(String companyState) {
        this.companyState = companyState;
    }

    public String getCompanyCountry() {
        return companyCountry;
    }

    public void setCompanyCountry(String companyCountry) {
        this.companyCountry = companyCountry;
    }

    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

    public String getIsSalesMan() {
        return isSalesMan;
    }

    public void setIsSalesMan(String isSalesMan) {
        this.isSalesMan = isSalesMan;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
