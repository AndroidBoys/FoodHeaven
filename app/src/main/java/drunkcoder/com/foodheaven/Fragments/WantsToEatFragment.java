package drunkcoder.com.foodheaven.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import drunkcoder.com.foodheaven.Adapters.ExpandableFoodListAdapter;
import drunkcoder.com.foodheaven.Models.Category;
import drunkcoder.com.foodheaven.Models.Food;
import drunkcoder.com.foodheaven.Models.Order;
import drunkcoder.com.foodheaven.MyApplication;
import drunkcoder.com.foodheaven.R;
import drunkcoder.com.foodheaven.ViewHolders.WantsToEatCategoryViewHolder;


public class WantsToEatFragment extends Fragment implements View.OnCreateContextMenuListener {

    private RecyclerView wantsToEatRecyclerView;
    //    private ListView wantsToEatCategoryListView;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private FirebaseRecyclerAdapter<Category, WantsToEatCategoryViewHolder> wantsToEatFoodAdapter;
    private DatabaseReference wantsToEatDatabaseReference;
    //    private int maxLimit;
    private ArrayList<Food> foodArrayList=new ArrayList<>();
    private ArrayList<Food> selectedFoodArrayList=new ArrayList<>();
    private ArrayList<String> foodNamesArrayList=new ArrayList<>();
    private ArrayList<String> foodItemUid=new ArrayList<>();
    //    private ArrayList<Category> categoryList=new ArrayList<>();
    private ArrayList<String> categoryNameList=new ArrayList<>();
    private HashMap<String,ArrayList<Food>> listFoodChild=new HashMap<>();
    private ArrayList<Integer> maxLimitOfCategory=new ArrayList<>();
    private ExpandableFoodListAdapter expandableFoodListAdapter;
    //    private int selectedCategory;
//    private Button wantAlertSelectButton;
    private Button wantsToEatSubmitButton;
    private EditText categoryNameEditText;
    private EditText maxLimitEditText;
    private Spinner timeSpinner;
    private ListView foodItemList;
    //    private Uri imageUri;
    private int selectedTime;
    //    private Spinner categorySpinner;
//    private CheckBox defaultCheckBox;
//    private Boolean isDefault=false;
    private ExpandableListView expandableListView;
    private EditText wantAlertFoodNameEditText;
    private CoordinatorLayout wantsToEatCoordinatorLayout;
    private FloatingActionButton wantsFloatingActionButton;
    private String mealTime="BreakFast";
//    private ArrayAdapter adapter;
//    ArrayList<ArrayList<String>> selectedFoodItems = new ArrayList<ArrayList<String>>();
    private ArrayList<Food> orderedFoodList[];
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.wants_to_eat_layout,container,false);
//        fetchUserList();
        context=getContext();


//        fetchAllFoodItems();


        wantsToEatDatabaseReference=FirebaseDatabase.getInstance().getReference("TodayMenu");
//        wantsToEatCoordinatorLayout=view.findViewById(R.id.wantsToEatCoordinatorLayout);
//        wantsFloatingActionButton=view.findViewById(R.id.wantsFloatingActionButton);
//        storageReference=FirebaseStorage.getInstance().getReference("images/");
        loadWantToEatImages(mealTime);

        wantsToEatSubmitButton=view.findViewById(R.id.wantsSubmitButton);
        wantsToEatSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CREATING ORDER
                createAndSubmitOrder();
            }
        });

        PullRefreshLayout wantsRefreshLayout=view.findViewById(R.id.wantsRefreshLayout);
        wantsRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWantToEatImages(mealTime);
            }
        });
        wantsRefreshLayout.setColor(R.color.colorPrimary);//set the color of refresh circle.

//        wantsSubmitButton.setButtonColor(getActivity().getResources().getColor(R.color.colorPrimary));

//        wantsFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showNewAlertDialog();
//            }
//        });

        expandableListView=view.findViewById(R.id.wantsToEatExpandableListView);
        expandableFoodListAdapter=new ExpandableFoodListAdapter(context,categoryNameList,listFoodChild);
        expandableListView.setAdapter(expandableFoodListAdapter);
//        expandableListView.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupIndex, int childIndex, long l) {
                Toast.makeText(context, "group "+groupIndex+"and child :"+childIndex, Toast.LENGTH_SHORT).show();
                CheckBox checkedTextView=view.findViewById(R.id.checkbox3);

                //since bydefault isChecked==false
                if(checkedTextView.isChecked()){
                    checkedTextView.toggle();//it will toggle the checkbox and onCheckChangeListener is called inside the expandable listview custom adapter
//                    Toast.makeText(context, "unchecked", Toast.LENGTH_SHORT).show();
//                    checkedTextView.setCheckMarkDrawable(R.drawable.ic_check_box_outline_blank_black_24dp);
                    orderedFoodList[groupIndex].remove((listFoodChild.get(categoryNameList.get(groupIndex))).get(childIndex));
                }
                else{
                    if(orderedFoodList[groupIndex].size()<maxLimitOfCategory.get(groupIndex)) {
                        checkedTextView.toggle();
//                        checkedTextView.setCheckMarkDrawable(R.drawable.ic_check_box_black_24dp);
//                        Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();
                        orderedFoodList[groupIndex].add((listFoodChild.get(categoryNameList.get(groupIndex))).get(childIndex));
                    }
                    else{
                        Toast.makeText(context, "You have reached to the limit", Toast.LENGTH_SHORT).show();
                    }
                }

                return false;
            }
        });

        //        wantsToEatRecyclerView=view.findViewById(R.id.wantsToEatRecyclerView);
//        wantsToEatCategoryListView=view.findViewById(R.id.wantsToEatListView);
//        adapter=new ArrayAdapter(context,android.R.layout.simple_list_item_1,categoryNameList);
//        wantsToEatCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });



//        foodChooseList=new ArrayList<>();
//        layoutManager=new LinearLayoutManager(context);
//        wantsToEatRecyclerView.setHasFixedSize(true);
//        wantsToEatRecyclerView.setLayoutManager(layoutManager);




        return view;
    }

    private void createAndSubmitOrder() {

        ArrayList<Food> finalFoodList=new ArrayList<>();
        //creating a single foodList from orderedFoodList[]
        for(int i=0;i<orderedFoodList.length;i++){
            for(int j=0;j<orderedFoodList[i].size();j++){
                finalFoodList.add(orderedFoodList[i].get(j));
            }
        }
        Order order=new Order(MyApplication.getCurrentUser(),0,finalFoodList);
        FirebaseDatabase.getInstance().getReference("Orders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(order);

        //Now placing current userUID inside all foods reference so that no. of user for a perticular food can be determined easily
        for(int i=0;i<finalFoodList.size();i++){
            FirebaseDatabase.getInstance().getReference("FavouriteFood").child(finalFoodList.get(i).getFoodName()).push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }


    }

//    private void fetchCategory(String mealTime) {
//        categoryList.clear();
//        categoryNameList.clear();
//    wantsToEatDatabaseReference.child(mealTime).addChildEventListener(new ChildEventListener() {
//        @Override
//        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//            Category category=dataSnapshot.getValue(Category.class);
//            categoryList.add(category);
//            categoryNameList.add(category.getCategoryName());
//            if(adapter!=null)
//                adapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//        }
//
//        @Override
//        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//        }
//
//        @Override
//        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//    });
//
//    }

    private void fetchAllFoodItems(){
        FirebaseDatabase.getInstance().getReference("FoodItems").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Food food=dataSnapshot.getValue(Food.class);
                foodArrayList.add(food);
                foodNamesArrayList.add(food.getFoodName());
                foodItemUid.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void loadWantToEatImages(String newMealTime) {
        mealTime = newMealTime;
        categoryNameList.clear();
        maxLimitOfCategory.clear();
        listFoodChild.clear();
        wantsToEatDatabaseReference.child(mealTime).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Category category = dataSnapshot.getValue(Category.class);
                categoryNameList.add(category.getCategoryName()+" max Limit ("+category.getMaxSelect()+")");
                maxLimitOfCategory.add(category.getMaxSelect());
                listFoodChild.put(category.getCategoryName()+" max Limit ("+category.getMaxSelect()+")", category.getFoodArrayList());
                expandableFoodListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //this is required to fetch the current orderedFoodList
        wantsToEatDatabaseReference.child(mealTime).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                orderedFoodList=new ArrayList[(int) dataSnapshot.getChildrenCount()];
                Log.d("childerencount",""+dataSnapshot.getChildrenCount());
                Log.d("orderfoodlistsize",""+orderedFoodList.length);
                for(int i=0;i<orderedFoodList.length;i++)
                    orderedFoodList[i]=new ArrayList<>();
                Log.d("listor",""+categoryNameList.size());
                expandableFoodListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static WantsToEatFragment newInstance() {

        Bundle args = new Bundle();

        WantsToEatFragment fragment = new WantsToEatFragment();
        fragment.setArguments(args);
        return fragment;
    }
}


























//package drunkcoder.com.foodheaven.Fragments;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.Toast;
//
//import com.baoyz.widget.PullRefreshLayout;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import drunkcoder.com.foodheaven.Models.SpecialFood;
//import drunkcoder.com.foodheaven.Models.FoodMenu;
//import drunkcoder.com.foodheaven.MyApplication;
//import drunkcoder.com.foodheaven.R;
//import drunkcoder.com.foodheaven.ViewHolders.FoodMenuViewHolder;
//import drunkcoder.com.foodheaven.ViewHolders.SpecialFoodViewHolder;
//import drunkcoder.com.foodheaven.ViewHolders.WantsToEatViewHolder;
//import info.hoang8f.widget.FButton;
//
//
//public class WantsToEatFragment extends Fragment {
//
//    private RecyclerView wantsToEatRecyclerView;
//    private RecyclerView.LayoutManager layoutManager;
//    private Context context;
//    private FirebaseRecyclerAdapter<FoodMenu, WantsToEatViewHolder> wantsToEatFoodAdapter;
//    private DatabaseReference wantsToEatDatabaseReference;
//    private FButton wantsSubmitButton;
//    private int maxLimit;
//    private ArrayList<FoodMenu> foodChooseList;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view=inflater.inflate(R.layout.wants_to_eat_layout,container,false);
//        context=getContext();
//        wantsToEatRecyclerView=view.findViewById(R.id.wantsToEatRecyclerView);
//        wantsToEatDatabaseReference=FirebaseDatabase.getInstance().getReference("FavouriteFood");
//        foodChooseList=new ArrayList<>();
//        wantsSubmitButton=view.findViewById(R.id.wantsSubmitButton);
//        layoutManager=new LinearLayoutManager(context);
//        wantsToEatRecyclerView.setHasFixedSize(true);
//        wantsToEatRecyclerView.setLayoutManager(layoutManager);
//        loadWantToEatImages();
//        wantsSubmitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if(foodChooseList.size()!=0){
//                    Toast.makeText(context,"Submitted Succesfully",Toast.LENGTH_SHORT).show();
//                    //make the entry in firebase
//
//                    for(int i=0;i<foodChooseList.size();i++){
//                        addChoosenFoodToFB(foodChooseList.get(i).getFoodName(),foodChooseList.get(i));
//                        //wantsToEatDatabaseReference.child("ChoosedBy").child(foodChooseList.get(i)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue()
//                    }
//                }
//
//            }
//        });
//
//        PullRefreshLayout wantsRefreshLayout=view.findViewById(R.id.wantsRefreshLayout);
//        wantsRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadWantToEatImages();
//            }
//        });
//        wantsRefreshLayout.setColor(R.color.colorPrimary);//set the color of refresh circle.
//
//        wantsSubmitButton.setButtonColor(getActivity().getResources().getColor(R.color.colorPrimary));
//
//        return view;
//    }
//
//    private void addChoosenFoodToFB(String foodName,FoodMenu foodMenu) {
//
//            FirebaseDatabase.getInstance().getReference("FavouriteFood").child("LikedFood").child(foodName).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                    .setValue(foodMenu);
//
//    }
//
//    private void loadWantToEatImages() {
//
//        DatabaseReference databaseReference=wantsToEatDatabaseReference.child("FoodImages");
//        wantsToEatDatabaseReference.child("maxLimit").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot!=null){
//                    maxLimit=dataSnapshot.getValue(Integer.class);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        wantsToEatFoodAdapter=new FirebaseRecyclerAdapter<FoodMenu, WantsToEatViewHolder>(FoodMenu.class,
//                R.layout.wants_to_eat_raw_layout,WantsToEatViewHolder.class,databaseReference) {
//            @Override
//            protected void populateViewHolder(WantsToEatViewHolder wantsToEatViewHolder, final FoodMenu foodMenu, int i) {
//                wantsToEatViewHolder.wantsFoodNameTextView.setText(foodMenu.getFoodName());
//                wantsToEatViewHolder.wantsFoodDescriptionTextView.setText(foodMenu.getFoodDescription());
//                Picasso.with(context).load(foodMenu.getImageUrl()).into(wantsToEatViewHolder.wantsFoodImageView);
//
//                wantsToEatViewHolder.wantsFoodCheckBox.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        CheckBox checkBox=(CheckBox)view;
//                        if(checkBox.isChecked()){
//                            if(foodChooseList.size()<maxLimit){
//                                foodChooseList.add(foodMenu);
//                            }else{
//                                checkBox.setChecked(false);
//                                Toast.makeText(context,"You can only select "+maxLimit+" items",Toast.LENGTH_LONG).show();
//                            }
//                        }else{
//                            Log.i("removed","------"+foodMenu.getFoodName());
//                            for(int j=0;j<foodChooseList.size();j++){
//                                if(foodChooseList.get(j).getFoodName().equals(foodMenu.getFoodName())){
//                                    Log.i("inside if",foodChooseList.get(j).getFoodName());
//                                    foodChooseList.remove(j);
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                });
//            }
//        };
//        wantsToEatRecyclerView.setAdapter(wantsToEatFoodAdapter);
//        //
//    }
//
//    public static WantsToEatFragment newInstance() {
//
//        Bundle args = new Bundle();
//
//        WantsToEatFragment fragment = new WantsToEatFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//}
