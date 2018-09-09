package drunkcoder.com.foodheaven.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import drunkcoder.com.foodheaven.Common.Common;

import static drunkcoder.com.foodheaven.Common.Common.todayOnlineDate;

public class OnlineTimeAsyncTask extends AsyncTask<String,Void,String> {

    public static final String TIME_SERVER = "time-a.nist.gov"; //fetching online date and time from this server

    @Override
    protected String doInBackground(String... strings) {
        Log.i("inside ","------------Asynck task");
        try {
            printTimes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void printTimes() throws IOException {
        Log.i("inside "," ----printtimes");
        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
        TimeInfo timeInfo = timeClient.getTime(inetAddress);
        //long returnTime = timeInfo.getReturnTime();   //local device time
        long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();   //server time

        Date time = new Date(returnTime);
        Log.i("getCurrentNetworkTime", "Time from " + TIME_SERVER + ": " + time);

        Log.i("Local time", "Local time");
        Log.i("Local time", "Current time: " + new Date(System.currentTimeMillis()));
        Log.i("Local time", "GetOriginateTimeStamp: " + new Date(timeInfo.getMessage().getOriginateTimeStamp().getTime()));


        Log.i("Local time", "Time info: " + new Date(timeInfo.getMessage().getReceiveTimeStamp().getTime()));

        Date date= timeInfo.getMessage().getReceiveTimeStamp().getDate();

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("IST")); //convert GMT time into IST time
        Common.todayOnlineDate= simpleDateFormat.format(date);
        Log.i("todays date","-----"+Common.todayOnlineDate);
    }
}
