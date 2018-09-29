package drunkcoder.com.foodheaven.Payments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import drunkcoder.com.foodheaven.Activities.HomeActivity;
import drunkcoder.com.foodheaven.Common.Common;
import drunkcoder.com.foodheaven.Models.NotificationSubscription;
import drunkcoder.com.foodheaven.Models.Plan;
import drunkcoder.com.foodheaven.Models.User;
import drunkcoder.com.foodheaven.Models.Wallet;
import drunkcoder.com.foodheaven.MyApplication;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.Utils.ProgressUtils;
import drunkcoder.com.foodheaven.Utils.SharedPreferenceUtil;
import info.hoang8f.widget.FButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class PaymentsActivity extends AppCompatActivity {

    private FButton paymentButton,paymentCashButton;
    private Plan choosenPlan;
    KProgressHUD progressHUD;

    private void initViews() {
        setContentView(R.layout.activity_payments);
        paymentButton = findViewById(R.id.payment_button);
        paymentCashButton = findViewById(R.id.payment_button_cash);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();

        choosenPlan = (Plan) getIntent().getSerializableExtra("choosenPlan");
        final String priceNo = calculatePrice(choosenPlan);
        String price = getResources().getString(R.string.Rupees) + priceNo;
        Log.i("price", "onCreate: "+price);
        paymentButton.setButtonColor(getResources().getColor(R.color.colorPrimary));
        paymentButton.setText("Proceed to pay "+price);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchPaymentFlow(priceNo);
            }
        });

        paymentCashButton.setButtonColor(getResources().getColor(R.color.colorPrimary));
        paymentCashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showCashPaymentAlert();
            }
        });

    }

    private void launchPaymentFlow(String price) {
        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
        payUmoneyConfig.setPayUmoneyActivityTitle("Payment for "+choosenPlan.getPlanName());
        payUmoneyConfig.setDoneButtonText("Let's explore the best food");

        User currentUser = MyApplication.currentUser;

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
        builder.setAmount(convertStringToDouble(price))
                .setTxnId(System.currentTimeMillis() + "")
                .setPhone(currentUser.getPhoneNumber())
                .setProductName(choosenPlan.getPlanName())
                .setFirstName(currentUser.getName())
                .setEmail(currentUser.getEmail())
                .setsUrl(Constants.SURL)
                .setfUrl(Constants.FURL)
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(Constants.DEBUG)
                .setKey(Constants.MERCHANT_KEY)
                .setMerchantId(Constants.MERCHANT_ID);

        try {
            PayUmoneySdkInitializer.PaymentParam mPaymentParams = builder.build();
            calculateHashInServer(mPaymentParams);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            paymentButton.setEnabled(true);
        }
    }

    private void calculateHashInServer(final PayUmoneySdkInitializer.PaymentParam mPaymentParams) {
        ProgressUtils.showLoadingDialog(this);
        String url = Constants.MONEY_HASH;
        StringRequest request = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        paymentButton.setEnabled(true);
                        String merchantHash = "";

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            merchantHash = jsonObject.getString("payment_hash");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ProgressUtils.cancelLoading();

                        if (merchantHash.isEmpty() || merchantHash.equals("")) {
                            Toast.makeText(PaymentsActivity.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
                        } else {
                            mPaymentParams.setMerchantHash(merchantHash);
                            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, PaymentsActivity.this,R.style.PayUMoney, false);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        paymentButton.setEnabled(true);
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(PaymentsActivity.this, "Connect to internet Volley", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PaymentsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        ProgressUtils.cancelLoading();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return mPaymentParams.getParams();
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {

            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            Log.i("payu", "payuresponse:"+transactionResponse.payuResponse+"payumessage"+transactionResponse.getMessage()+"payutransactiondetails:"+transactionResponse.getTransactionDetails()+"transactionStatus"+transactionResponse.getTransactionStatus().name());

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                Log.i("payu", "payuresponse:"+transactionResponse.payuResponse+"payumessage"+transactionResponse.getMessage()+"payutransactiondetails:"+transactionResponse.getTransactionDetails()+"transactionStatus"+transactionResponse.getTransactionStatus().name());
                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    showAlert("Payment Successful");
                    onSuccesfulPayment();
                } else if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.CANCELLED)) {
                    showAlert("Payment Cancelled:"+transactionResponse.getMessage()+" "+transactionResponse.getPayuResponse());
                } else if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.FAILED)) {
                    showAlert("Payment Failed");
                }

            } else if (resultModel != null && resultModel.getError() != null) {
                Toast.makeText(this, "Error check log", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Both objects are null", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_CANCELED) {
            showAlert("Payment Cancelled");
        }
    }

    private Double convertStringToDouble(String str) {
        return Double.parseDouble(str);
    }

    private void showAlert(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showCashPaymentAlert(){
        progressHUD = KProgressHUD.create(this);
        progressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        progressHUD.setCancellable(false);
        progressHUD.setLabel("Please wait");

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("In cash Payment");
        alertDialog.setMessage("You can pay for this plan in cash on the first delievery and your services will be activated by our executives afterwards");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Lets talk about it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                processCashPayments();
                progressHUD.show();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();

    }

    private void onSuccesfulPayment()
    {
        updateUserSubscription();
    }

    private void subscribeToNotifications() {

        User user = MyApplication.currentUser;
        FirebaseMessaging messaging =FirebaseMessaging.getInstance();
        DatabaseReference subRef = FirebaseDatabase.getInstance().getReference("NotificationSubscriptions");
        NotificationSubscription subscription = new NotificationSubscription();
        subscription.setToken(SharedPreferenceUtil.getSavedNotificationToken(this));
        if(user.getSubscribedPlan().includesDinner){

            messaging.subscribeToTopic("Dinner");
            subscription.setSubscribedToDinner(true);

        }
        if(user.getSubscribedPlan().includesLunch){

            messaging.subscribeToTopic("Lunch");
            subscription.setSubscribedToLunch(true);
        }
        if(user.getSubscribedPlan().includesBreakFast){

            messaging.subscribeToTopic("BreakFast");
            subscription.setSubscribedToBreakFast(true);
        }

        subRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(subscription);

    }

    private void updateCurrentUser() {

        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        MyApplication.currentUser = dataSnapshot.getValue(User.class);

                        //subscribe to what do you want to eat today notification
                        subscribeToNotifications();

                        paymentButton.setEnabled(true);
                        paymentButton.setText("Payment succesful , Let's Go");
                        paymentButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(PaymentsActivity.this, HomeActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void updateUserSubscription(){
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("subscribedPlan").setValue(choosenPlan).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateUserWallet();
            }
        });

    }

    private void updateUserWallet(){

        String amount = calculatePrice(choosenPlan);

        String dueDate = calculateDueDate1();


        if(dueDate!=null) {
            Wallet wallet = new Wallet();
//        wallet.setAvailableBalance(amount);
            wallet.setCreditedAmount(amount);
            wallet.setDueDate(dueDate);
            wallet.setRemainingDays(choosenPlan.getNoOfDays());

            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("wallet").setValue(wallet).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    updateCurrentUser();
                }
            });
        }
        // 9690300349

    }

    public String calculateDueDate(){

        String dateInString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance(); // Get Calendar Instance
        try {
            c.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        c.add(Calendar.DATE, Integer.parseInt(choosenPlan.getNoOfDays()));
        sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date resultdate = new Date(c.getTimeInMillis());   // Get new time
        dateInString = sdf.format(resultdate);
        return  dateInString;
    }

    public String calculateDueDate1(){

        if(Common.todayOnlineDate!=null) {
            String todayDate = Common.todayOnlineDate;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(simpleDateFormat.parse(todayDate));//Setting todayDate into calendar variable.
                // So that we can add them later one
            } catch (ParseException e) {
                e.printStackTrace();
            }
            calendar.add(Calendar.DATE, Integer.parseInt(choosenPlan.getNoOfDays()));
            Date resultDate = new Date(calendar.getTimeInMillis()); //we are getting the timeInMillis after adding dates
            return simpleDateFormat.format(resultDate);
        }
        return null;
    }

    private void processCashPayments(){

        DatabaseReference subRef = FirebaseDatabase.getInstance().getReference("NotificationSubscriptions");
        NotificationSubscription subscription = new NotificationSubscription();
        subscription.setToken(SharedPreferenceUtil.getSavedNotificationToken(this));
        subRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(subscription).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               buyPlanWithCash();
               // Toast.makeText(PaymentsActivity.this, msg, Toast.LENGTH_LONG).show();
              // startActivity(new Intent(PaymentsActivity.this,HomeActivity.class));
            }
        });
    }

    private void buyPlanWithCash(){
        FirebaseDatabase.getInstance().getReference("Requests").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(choosenPlan).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressHUD.dismiss();
                String msg="Congratulations! for your first step toward the best tiffin service. Our executives will shortly talk to you";
                showAlert(msg);
            }
        });
    }

    private String calculatePrice(Plan plan){
        int frequency =0;
        if(plan.includesBreakFast){
            frequency++;
        }
        if(plan.includesLunch){
            frequency++;
        }
        if(plan.includesDinner){
            frequency++;
        }

        switch (frequency){
            case 1:
                return plan.getOneTimePrice();
            case 2:
                return plan.getTwoTimePrice();
            case 3:
                return plan.getThreeTimePrice();
        }

        return "";
    }
    private int getInt(String s){
        return  Integer.parseInt(s);
    }

//
}
