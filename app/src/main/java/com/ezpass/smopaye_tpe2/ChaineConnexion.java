package com.ezpass.smopaye_tpe2;

public class ChaineConnexion {


    //private static final String adresseURLsmopayeServer = "https://cm.secure-ws-api-smp-excecute.smopaye.fr/index.php";
    private static final String adresseURLsmopayeServer = "https://webservice.domaine.tests.space.smopaye.fr";
    private static final String adresseURLGoogleAPI = "https://fcm.googleapis.com/";
    private static final String urlSiteWeb = "https://smopaye.cm/";
    private static final String encrypted_password = "Iyz4BVU2Hlt0cIeIPBlB7Wq15kMDI4NGRmOTNi";
    private static final String salt = "d0284df93b";
    private static final String security_keys = "56ZS5PQ1RF-eyJsaWNlbnNlSWQiOiI1NlpGVkIjpmYWxzZX0+-==";



    public static String getAdresseURLsmopayeServer() {
        return adresseURLsmopayeServer;
    }

    public static String getAdresseURLGoogleAPI() {
        return adresseURLGoogleAPI;
    }

    public static String getUrlSiteWeb() {
        return urlSiteWeb;
    }

    public static String getEncrypted_password() {
        return encrypted_password;
    }

    public static String getSalt() {
        return salt;
    }

    public static String getsecurity_keys() {
        return security_keys;
    }

}
