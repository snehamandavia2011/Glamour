package utility;

import android.content.Context;

import glamour.mafatlal.com.glamour.R;

/**
 * Created by SAI on 8/4/2017.
 */

public class ConstantVal {
    public static final String IS_SESSION_EXISTS = "is_session_exists";

    public static class UserType {
        public static final String RETAILER = "Retailer";
        public static final String DEALER = "Dealer";
    }

    public static class BroadcastAction {
        public static final String SESSION_EXPIRE = "jobio.io.SESSION_EXPIRE";
    }

    public static class ToastBGColor {
        public static final int SUCCESS = R.color.Success;
        public static final int INFO = R.color.info;
        public static final int WARNING = R.color.warning;
        public static final int DANGER = R.color.danger;
    }

    public static class ServerResponseCode {
        public static final String SESSION_EXISTS = "1";//value receive from server as response
        public static final String NO_INTERNET = "001";
        public static final String PARSE_ERROR = "002";
        public static String SERVER_NOT_RESPONDING = "003";
        public static String REQUEST_TIMEOUT = "004";//30 seconds
        public static String INVALID_LOGIN = "006";//value receive from server as response
        public static String SERVER_ERROR = "007";
        public static String SUCCESS = "008";
        public static String CLIENT_ERROR = "010";
        public static String BLANK_RESPONSE = "011";

        public static String getMessage(Context ctx, String strCode) {
            try {
                int intCode = Integer.parseInt(strCode);
                if (intCode == Integer.parseInt(NO_INTERNET)) {
                    return ctx.getString(R.string.strInternetNotAvaiable);
                } else if (intCode == Integer.parseInt(PARSE_ERROR)) {
                    return ctx.getString(R.string.strUnableToParseData);
                } else if (intCode == Integer.parseInt(SERVER_NOT_RESPONDING)) {
                    return ctx.getString(R.string.strServerNotResponding);
                } else if (intCode == Integer.parseInt(REQUEST_TIMEOUT)) {
                    return ctx.getString(R.string.strRequestTimeout);
                }  else if (intCode == Integer.parseInt(INVALID_LOGIN)) {
                    return ctx.getString(R.string.strInvalidUserNameAndPassword);
                } else if (intCode == Integer.parseInt(SERVER_ERROR)) {
                    return ctx.getString(R.string.strServerError);
                } else if (intCode == Integer.parseInt(SUCCESS)) {
//
                } else if (intCode == Integer.parseInt(CLIENT_ERROR)) {
                    return ctx.getString(R.string.strClientError);
                } else if (intCode == Integer.parseInt(BLANK_RESPONSE)) {
                    return ctx.getString(R.string.strDatacannotReceive);
                }
                return strCode;
            } catch (NumberFormatException e) {
                Logger.writeToCrashlytics(e);
                return strCode;
            }
        }
    }

    private static String getWebURLPrefix() {
        return "https://stackio.co/mWebApi/v1/";
        //return "http://" + QRCode + ".gojobio.co/mWebApi/v1/";
        //return "http://10.0.2.2:80/Electrasync_API_new/index.php/";//for emulator
        //return "http://10.0.3.2/jobio_mobile_webapi/index.php/";//for genymotion
    }

    public static URLMapping customerCredentialVerification() {
        String[] paramNames = {"email_id", "password", "user_type"};
        String URL = getWebURLPrefix() + "Credentialsmanager/customerCredentialVerification";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping checkUserExistence() {
        String[] paramNames = {"email_id"};
        String URL = getWebURLPrefix() + "Credentialsmanager/checkUserExistence";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping resetPassword() {
        String[] paramNames = {"email_id"};
        String URL = getWebURLPrefix() + "Credentialsmanager/resetPassword";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping registerUser() {
        String[] paramNames = {"fisrtName", "lastName", "emailId", "password", "mobileNumber", "userType", "companyName", "companyPhone", "companyHouseNo", "companyStreet", "companyLandmark", "companyCity", "companyState", "companyCountry", "companyPostCode", "isOwner", "isSalesMan", "date", "time"};
        String URL = getWebURLPrefix() + "Credentialsmanager/registerUser";
        return new URLMapping(paramNames, URL);
    }
}
