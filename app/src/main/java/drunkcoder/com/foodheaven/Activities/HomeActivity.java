package drunkcoder.com.foodheaven.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import drunkcoder.com.foodheaven.AsyncTasks.OnlineTimeAsyncTask;
import drunkcoder.com.foodheaven.Fragments.SubscribedUserFragment;
import drunkcoder.com.foodheaven.Fragments.UnsubscribedUser;
import drunkcoder.com.foodheaven.Fragments.UserProfileFragment;
import drunkcoder.com.foodheaven.MyApplication;
import drunkcoder.com.foodheaven.R;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int R_id_profileId=1000;
    private ImageView navigationImageView;
    private TextView nameNavigationTextView;
    private TextView emailNavigationTextView;
    private NavigationView navigationView;

    public static boolean isAppRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Before this we have to check internet connection
        OnlineTimeAsyncTask onlineTimeAsyncTask=new OnlineTimeAsyncTask();
        onlineTimeAsyncTask.execute();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i("Token", "onCreate: "+FirebaseInstanceId.getInstance().getToken());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }

// <<<<<<< 24-sept
//         if(MyApplication.getCurrentUser().getSubscribedPlan()==null) {
//             addDifferentFragment(UnsubscribedUser.newInstance());
// =======

        navigationView= findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if(MyApplication.getCurrentUser().getSubscribedPlan()==null) {
            addDifferentFragment(UnsubscribedUser.newInstance(),"subscribe");
            navigationView.setCheckedItem(R.id.nav_home);
// >>>>>>> master
        }
        else{
            navigationView.setCheckedItem(R.id.nav_home);
            addDifferentFragment(SubscribedUserFragment.newInstance(),"unsubscribe");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        View header = navigationView.getHeaderView(0);
        nameNavigationTextView = header.findViewById(R.id.nameNavigationTextView);
        navigationImageView = header.findViewById(R.id.navigationImageView);
        emailNavigationTextView = header.findViewById(R.id.emailNavigationtextView);

        setCurrentUserInfoInNavigationBar();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id!=R.id.nav_logout)
        item.setChecked(true);

            if (id == R.id.nav_home) {
                if(MyApplication.getCurrentUser().getSubscribedPlan()==null) {
                    addDifferentFragment(UnsubscribedUser.newInstance(),"subscribe");
                }else{
                    addDifferentFragment(SubscribedUserFragment.newInstance(),"unsubscribe");
                }
                // Handle the camera action
            } else if (id == R.id.nav_weeklyMenu) {

                Intent intent = new Intent(HomeActivity.this, DescriptionActivity.class);
                intent.putExtra("ID", R.id.weeklyMenuButton);//since we have to show the weeklyMenu on the screen which will be host by the description activity
                startActivity(intent);
            }
              else if (id == R.id.nav_specialOrder) {
                if(MyApplication.getCurrentUser().getSubscribedPlan()!=null) {
                    SubscribedUserFragment subscribedUserFragment = SubscribedUserFragment.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putInt("POSITION", 1);//SINCE position of the special order position is 1 in view pager
                    subscribedUserFragment.setArguments(bundle);
                    addDifferentFragment(subscribedUserFragment,null);
                }else{
                    Toast.makeText(this, "Please subscribe for our plans first!", Toast.LENGTH_SHORT).show();
                }
            }
            else if (id == R.id.nav_contactUs) {
            }
            else if (id == R.id.nav_logout) {
                logOutDialog();
            }
            else if (id == R.id.nav_wallet) {
                if(MyApplication.getCurrentUser().getSubscribedPlan()!=null) {
                    SubscribedUserFragment subscribedUserFragment = SubscribedUserFragment.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putInt("POSITION", 2);//SINCE position of the wallet position is 1 in view pager
                    subscribedUserFragment.setArguments(bundle);
                    addDifferentFragment(subscribedUserFragment,null);
                }else{
                    Toast.makeText(this, "Please subscribe for our plans first!", Toast.LENGTH_SHORT).show();
                }
            }else if (id == R.id.nav_rate) {
                addDifferentFragment(UnsubscribedUser.newInstance(),"unsubscribe");
            }else if (id == R.id.nav_profile) {

                Intent intent = new Intent(HomeActivity.this, DescriptionActivity.class);
                intent.putExtra("ID", R_id_profileId);//since we have to show the profile on the screen which will be host by the description activity
                startActivity(intent);
            }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void setCurrentUserInfoInNavigationBar() {

        nameNavigationTextView.setText(MyApplication.getCurrentUser().getName());
        emailNavigationTextView.setText(MyApplication.getCurrentUser().getEmail());

        TextDrawable textDrawable = TextDrawable.builder()
                .buildRound(""+"" + MyApplication.getCurrentUser().getName().charAt(0),R.color.md_deep_purple_700);//setting first letter of the user name
        navigationImageView.setImageDrawable(textDrawable);
    }

    private void addDifferentFragment(Fragment replacableFragment,String tag){
        Log.i("Inside","Different fragment function");
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,replacableFragment,tag).commit();
    }


    public void logOut()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null)
        {
            FirebaseAuth.getInstance().signOut();
            moveToAuthenticationActivity();
        }

    }

    public void moveToAuthenticationActivity()
    {
//        Intent intent = new Intent(this,AuthenticationActivity.class);
//        startActivity(intent);
          finish();
    }

    public void logOutDialog()
    {
        new AlertDialog.Builder(this)
                .setMessage("You will be loged out")
                .setTitle("Do you really want to log out?")
                .setIcon(R.drawable.thali_graphic)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        item.setChecked(false);
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logOut();
                    }
                }).show();
    }




//    public void notification(){
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        String channelId = "1";
//        String channel2 = "2";
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(channelId,
//                    "Channel 1",NotificationManager.IMPORTANCE_HIGH);
//
//            notificationChannel.setDescription("This is BNT");
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.enableVibration(true);
//            notificationChannel.setShowBadge(true);
//            notificationManager.createNotificationChannel(notificationChannel);
//
//            NotificationChannel notificationChannel2 = new NotificationChannel(channel2,
//                    "Channel 2", NotificationManager.IMPORTANCE_MIN);
//
//            notificationChannel.setDescription("This is bTV");
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.enableVibration(true);
//            notificationChannel.setShowBadge(true);
//            notificationManager.createNotificationChannel(notificationChannel2);
//
//        }
//
//
//
//    }

    @Override
    public void onBackPressed() {
        Fragment fragment;
        if(MyApplication.getCurrentUser().getSubscribedPlan()!=null){
            fragment=getSupportFragmentManager().findFragmentByTag("subscribe");
        }else{
            fragment=getSupportFragmentManager().findFragmentByTag("unsubscribe");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(MyApplication.getCurrentUser().getSubscribedPlan()!=null && !(fragment instanceof SubscribedUserFragment)){
            addDifferentFragment(SubscribedUserFragment.newInstance(),"subscribe");
            navigationView.setCheckedItem(R.id.nav_home);
        }
        else if(MyApplication.getCurrentUser().getSubscribedPlan()==null && !(fragment instanceof UnsubscribedUser)){
            addDifferentFragment(UnsubscribedUser.newInstance(),"unsubscribe");
            navigationView.setCheckedItem(R.id.nav_home);
        }
        else{
            exitAlertDialog();
        }
    }

    private void exitAlertDialog() {

        new AlertDialog.Builder(this)
                .setMessage("You will exit this app")
                .setTitle("Do you really want to exit?")
                .setIcon(R.drawable.thali_graphic)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                        finish();
                    }
                }).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAppRunning=false;
    }
}
