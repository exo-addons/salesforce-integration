public class ConfigurationManager{

    //global endpoint
    public static String CALLOUT_ENDPOINT{
        get{
            return 'http://plfent-4.3.x-pkgpriv-salesforce-integration-snapshot.acceptance5.exoplatform.org/rest/';
        }
    }

    //global timeout in ms
    public static Integer CALLOUT_TIMEOUT{
        get{
            return 60000;
        }
    }

//str token to set in the request header
    public static String SID{
        get{
            return 'AC501b74a568e3599440e5303377fae7b9'; 
        }
    }


    //user/profile centric debug mode
    public static Boolean DEBUG_MODE{
        get{
            return false;
        }
    }

    //returns a specific date format given the locale code
    public static String getDateFormat(String localeCode){
        if(localeCode == 'en_US'){
            return 'dd/MM/yyyy';
        }
        return 'MM/dd/yyyy';
    }

}