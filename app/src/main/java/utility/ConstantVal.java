package utility;

import android.content.Context;

import java.util.ArrayList;

import entity.Category;
import entity.ProductMaster;
import entity.SizeMaster;
import glamour.mafatlal.com.glamour.R;

/**
 * Created by SAI on 8/4/2017.
 */

public class ConstantVal {
    public static final String IS_SESSION_EXISTS = "is_session_exists";
    public static ArrayList<SizeMaster> arrSizeMaster;
    public static ArrayList<SizeMaster> ARR_SELECTED_SIZE = new ArrayList<>();
    public static int MIN_PRICE;
    public static int MAX_PRICE;
    public static int SELECTED_MIN_PRICE;
    public static int SELECTED_MAX_PRICE;
    public static ArrayList<ProductMaster> arrProduct;
    public static final String DATE_TIME_FORMAT = "dd MMM, yyyy HH:mm";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final int REQUEST_REFINE = 1;
    public static final int RESPONSE_REFINE = 2;

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
        public static String SESSION_EXPIRED = "005";//value
        public static String INVALID_LOGIN = "006";//value receive from server as response
        public static String SERVER_ERROR = "007";
        public static String SUCCESS = "008";
        public static String CLIENT_ERROR = "010";
        public static String BLANK_RESPONSE = "011";
        public static String USER_ALREADY_EXISTS = "012";

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
                } else if (intCode == Integer.parseInt(SESSION_EXPIRED)) {
                    return ctx.getString(R.string.strSessionExpire);
                } else if (intCode == Integer.parseInt(INVALID_LOGIN)) {
                    return ctx.getString(R.string.strInvalidUserNameAndPassword);
                } else if (intCode == Integer.parseInt(SERVER_ERROR)) {
                    return ctx.getString(R.string.strServerError);
                } else if (intCode == Integer.parseInt(SUCCESS)) {
//
                } else if (intCode == Integer.parseInt(CLIENT_ERROR)) {
                    return ctx.getString(R.string.strClientError);
                } else if (intCode == Integer.parseInt(BLANK_RESPONSE)) {
                    return ctx.getString(R.string.strDatacannotReceive);
                } else if (intCode == Integer.parseInt(USER_ALREADY_EXISTS)) {
                    return ctx.getString(R.string.strEmailIdAlreadyExists);
                }
                return strCode;
            } catch (NumberFormatException e) {
                Logger.writeToCrashlytics(e);
                return strCode;
            }
        }
    }

    private static String getWebURLPrefix() {
        //return "https://stackio.co/mWebApi/v1/";
        return "http://45.249.111.13/~glamourapp/mWebApi/v1/";
    }

    public static URLMapping customerCredentialVerification() {
        String[] paramNames = {"email_id", "password", "android_id", "deviceName", "deviceVersion", "date", "time"};
        String URL = getWebURLPrefix() + "Credentialsmanager/customerCredentialVerification";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping checkUserExistence() {
        String[] paramNames = {"email_id"};
        String URL = getWebURLPrefix() + "Credentialsmanager/checkUserExistence";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping resetPassword() {
        String[] paramNames = {"to_email", "date", "time"};
        String URL = getWebURLPrefix() + "Emailmanager/resetPassword";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping registerUser() {
        String[] paramNames = {"fisrtName", "lastName", "emailId", "mobileNumber", "userType", "companyName", "companyPhone", "companyHouseNo", "companyStreet", "companyLandmark", "companyCity", "companyState", "companyCountry", "companyPostCode", "isOwner", "isSalesMan", "date", "time"};
        String URL = getWebURLPrefix() + "Credentialsmanager/registerUser";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping updatePersonalInfo() {
        String[] paramNames = {"fisrtName", "lastName", "emailId", "mobileNumber", "userType", "token"};
        String URL = getWebURLPrefix() + "Credentialsmanager/updatePersonalInfo";
        return new URLMapping(paramNames, URL);
    }


    public static URLMapping updateCompanyDetail() {
        String[] paramNames = {"emailId", "companyName", "companyPhone", "isOwner", "isSalesMan", "token"};
        String URL = getWebURLPrefix() + "Credentialsmanager/updateCompanyDetail";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping updateCompanyAddress() {
        String[] paramNames = {"emailId", "companyHouseNo", "companyStreet", "companyLandmark", "companyCity", "companyState", "companyCountry", "companyPostCode", "token"};
        String URL = getWebURLPrefix() + "Credentialsmanager/updateCompanyAddress";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping changePassword() {
        String[] paramNames = {"emailId", "new_password", "token"};
        String URL = getWebURLPrefix() + "Credentialsmanager/changePassword";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping logoutUser() {
        String[] paramNames = {"userID", "token"};
        String URL = getWebURLPrefix() + "Credentialsmanager/logoutUser";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping getProductCategory() {
        String[] paramNames = {"token"};
        String URL = getWebURLPrefix() + "Productmanager/getProductCategory";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping loadPhoto() {
        String[] paramNames = {"id", "table_index", "token"};//0:category
        String URL = getWebURLPrefix() + "Common/loadPhoto";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping getColorList() {
        String[] paramNames = {"token"};//0:category
        String URL = getWebURLPrefix() + "Common/getColorList";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping getSizeList() {
        String[] paramNames = {"token"};//0:category
        String URL = getWebURLPrefix() + "Common/getSizeList";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping getProductList() {
        String[] paramNames = {"token", "category_id"};
        String URL = getWebURLPrefix() + "Productmanager/getProductList";
        return new URLMapping(paramNames, URL);
    }

    public static URLMapping placeOrder() {
        String[] paramNames = {"device_order_id","token", "customer_id", "order_item", "date", "time"};
        String URL = getWebURLPrefix() + "Productmanager/placeOrder";
        return new URLMapping(paramNames, URL);
    }
}
