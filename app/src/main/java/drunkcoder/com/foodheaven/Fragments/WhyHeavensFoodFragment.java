package drunkcoder.com.foodheaven.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.UUID;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.Models.WhyHeavenFood;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.ViewHolders.WhyHeavensFoodViewHolder;

public class WhyHeavensFoodFragment extends Fragment {
    private RecyclerView whyHeavensFoodRecyclerView;
    private DatabaseReference databaseReference;
    private FloatingActionButton addDescriptionFloatingActionButton;
    private final int PICK_IMAGE=100;
    private Uri imageUri=null;//this variable changes according to image selecte from galary.
    private EditText aboutEditText;
    private Button chooseImageButton;
    private  FirebaseRecyclerAdapter<WhyHeavenFood,WhyHeavensFoodViewHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.why_heaven_foods_fragment,container,false);
//        addDescriptionFloatingActionButton=view.findViewById(R.id.addWhyHeavensFood);
        whyHeavensFoodRecyclerView=view.findViewById(R.id.whyHeavenFoodsRecyclerView);
        whyHeavensFoodRecyclerView.setHasFixedSize(true);
        whyHeavensFoodRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchAboutDataFromFirebase();

        return view;
    }

    private void fetchAboutDataFromFirebase() {
        databaseReference=FirebaseDatabase.getInstance().getReference("WhyHeavensFood");

        adapter= new FirebaseRecyclerAdapter<WhyHeavenFood, WhyHeavensFoodViewHolder>(WhyHeavenFood.class,R.layout.why_heaven_foods_fragment_row,WhyHeavensFoodViewHolder.class,databaseReference) {
            @Override
            protected void populateViewHolder(WhyHeavensFoodViewHolder whyHeavensFoodViewHolder, WhyHeavenFood whyHeavenFood, int i) {
                setData(whyHeavensFoodViewHolder,whyHeavenFood);

            }
        };
        adapter.notifyDataSetChanged();

        whyHeavensFoodRecyclerView.setAdapter(adapter);
    }

    private void setData(WhyHeavensFoodViewHolder whyHeavensFoodViewHolder,WhyHeavenFood whyHeavenFood) {
        Picasso.with(getContext()).load(whyHeavenFood.getImageUrl()).into(whyHeavensFoodViewHolder.aboutImageView);
        whyHeavensFoodViewHolder.aboutTextView.setText(whyHeavenFood.getAbout());
    }

    public WhyHeavensFoodFragment() {
    }

    public static WhyHeavensFoodFragment newInstance() {

        Bundle args = new Bundle();

        WhyHeavensFoodFragment fragment = new WhyHeavensFoodFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
