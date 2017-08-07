package utility;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import entity.User;

public class HttpEngine {

    public ServerResponse getDataFromWebAPI(Context mContext, String strURL, String[] paramNames,String[] paramValues) {


        ServerResponse objServerResponse = null;
        String data = "";
        try {
            for (int i = 0; i < paramNames.length; i++) {
                data += "&" + URLEncoder.encode(paramNames[i], "UTF-8")
                        + "=" + URLEncoder.encode(paramValues[i], "UTF-8");
            }
            if (!data.equals(""))
                data = data.substring(1, data.length());
            Logger.debug("[URL:" + strURL + "][Data:" + data + "]");

            objServerResponse = makeHttpRequestCall(mContext, strURL, data);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
        }
        return objServerResponse;
    }

    public ServerResponse makeHttpRequestCall(Context mContext, String strURL, String data) {
        ServerResponse objServerResponse = null;
        int resoponseCode = 0;
        String serverResponseMessage = "";
        String strResponse = "";
        try {
            if (!isNetworkAvailable(mContext)) {
                objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.NO_INTERNET, ConstantVal.ServerResponseCode.NO_INTERNET);
            } else {
                URL url = new URL(strURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(10000);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(data);
                wr.flush();
                urlConnection.connect();
                resoponseCode = urlConnection.getResponseCode();
                serverResponseMessage = urlConnection.getResponseMessage();
                Logger.debug("[URL:" + strURL + "][SERVER RESPONSE CODE:" + resoponseCode + "][SERVER RESPONSE MESSAGE:" + serverResponseMessage + "]");
                if (resoponseCode >= 500 && resoponseCode <= 520) {
                    objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.SERVER_ERROR, ConstantVal.ServerResponseCode.SERVER_ERROR);
                } else if (resoponseCode >= 400 && resoponseCode <= 451) {
                    objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.CLIENT_ERROR, ConstantVal.ServerResponseCode.CLIENT_ERROR);
                } else {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuffer res = new StringBuffer();
                    char[] chBuff = new char[1000];
                    int len = 0;
                    while ((len = in.read(chBuff)) > 0)
                        res.append(new String(chBuff, 0, len));
                    in.close();
                    urlConnection.disconnect();
                    strResponse = res.toString();
                    if (strResponse == null || strResponse.equals("")) {
                        objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.BLANK_RESPONSE, ConstantVal.ServerResponseCode.BLANK_RESPONSE);
                    } else if (strResponse.equals(ConstantVal.ServerResponseCode.INVALID_LOGIN)) {
                        objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.INVALID_LOGIN, ConstantVal.ServerResponseCode.INVALID_LOGIN);
                    } else if (strResponse.equals(ConstantVal.ServerResponseCode.SESSION_EXISTS)) {
                        objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.SESSION_EXISTS, ConstantVal.ServerResponseCode.SESSION_EXISTS);
                    } else if (strResponse.equals(ConstantVal.ServerResponseCode.SUCCESS)) {
                        objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.SUCCESS, ConstantVal.ServerResponseCode.SUCCESS);
                    } else {
                        boolean isValid = Helper.isValidJSON(strResponse);
                        if (!isValid) {
                            objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.PARSE_ERROR, ConstantVal.ServerResponseCode.PARSE_ERROR);
                        } else {
                            objServerResponse = new ServerResponse(strResponse, ConstantVal.ServerResponseCode.SUCCESS);
                            //result=result;
                        }
                    }
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.REQUEST_TIMEOUT, ConstantVal.ServerResponseCode.REQUEST_TIMEOUT);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.CLIENT_ERROR, ConstantVal.ServerResponseCode.CLIENT_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.REQUEST_TIMEOUT, ConstantVal.ServerResponseCode.REQUEST_TIMEOUT);
        } catch (Exception e) {
            objServerResponse = new ServerResponse(ConstantVal.ServerResponseCode.BLANK_RESPONSE, ConstantVal.ServerResponseCode.BLANK_RESPONSE);
            Logger.writeToCrashlytics(e);
            e.printStackTrace();
        }
        Logger.debug("[URL:" + strURL + "][Application response code" + objServerResponse.getResponseCode() + "][RESPONSE FROM API:" + strResponse + "]");
        String message = "[User id:" + Helper.getStringPreference(mContext, User.Fields.EMAILID, "") + "] [URL:" + strURL + "] [Data:" + data + "] [Server response code:" + resoponseCode + "] [Server message:" + serverResponseMessage + "] [application response Code:" + objServerResponse.getResponseCode() + "] [application response:" + objServerResponse.getResponseString() + "]";
        Logger.writeToCrashlytics(message);
        return objServerResponse;
    }

    public boolean isNetworkAvailable(Context mContext) {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            //Toast.makeText(mContext, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            //Toast.makeText(mContext, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

}
