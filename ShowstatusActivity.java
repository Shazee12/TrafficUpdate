package shahzaib.com.trafficupdate.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import shahzaib.com.trafficupdate.Model.Postitem;
import shahzaib.com.trafficupdate.Model.RessultModel;
import shahzaib.com.trafficupdate.Model.latlng;
import shahzaib.com.trafficupdate.R;

/**
 * Created by shahzaib on 7/28/2017.
 */

public  class ShowstatusActivity extends AppCompatActivity {


    private ArrayList<latlng> latLng;
    DatabaseReference mFirebaseDatabaseReference;
    FirebaseDatabase database;
    RecyclerView postlist;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter<PostViewHolder> adapter;
    ArrayList<RessultModel> postitemArrayList;
    private Toolbar mToolbar;
    private  ArrayList<latlng> lt;
    public HashSet<String> names = new HashSet<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_showstatus);
        super.onCreate(savedInstanceState);

        mToolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        lt = new ArrayList<>();
         latLng = new ArrayList<>();
        latLng = intent.getParcelableArrayListExtra("latlngs");
        postlist = (RecyclerView) findViewById(R.id.postsstatus);

        names = (HashSet<String>) intent.getSerializableExtra("names");
        Log.d("qqq", String.valueOf((latLng)));

        layoutManager = new LinearLayoutManager(this);
        postitemArrayList = new ArrayList<>();

        //postitemArrayList.clear();

        adapter = new Postadapters(postitemArrayList);

        database = FirebaseDatabase.getInstance();

        mFirebaseDatabaseReference = database.getReference();

        postlist.setLayoutManager(layoutManager);

        postlist.setAdapter(adapter);
    /*mFirebaseDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot snapshot:dataSnapshot.child("latLng").getChildren()){
                RessultModel model = dataSnapshot.getValue(RessultModel.class);
                lt = model.getLatLng();
            }
            Toast.makeText(getApplicationContext(), "m" +  lt, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });*/
        mFirebaseDatabaseReference.child("Posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RessultModel model = dataSnapshot.getValue(RessultModel.class);
                Log.d("yyyy firebase values", String.valueOf((model.getLatLng())));
                Log.d("yyyy filter  values", String.valueOf((latLng)));

                for (latlng c : latLng) {
                    if (model != null && model.getLatLng().equals(c)) {
                        // Log.d("Names", model.getLatLng());
                        Log.d("Latlng", String.valueOf(model.getLatLng()));
                        postitemArrayList.add(model);
                        // Collections.reverse(postitemArrayList);
                        adapter.notifyItemInserted(postitemArrayList.size() - 1);
                    }
            }

            }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    class Postadapters extends RecyclerView.Adapter<PostViewHolder> {

        ArrayList<RessultModel> postItems;

         Postadapters(ArrayList<RessultModel> postItems) {
            this.postItems = postItems;
        }

        @Override
        public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_items,parent,false));
        }

        @Override
        public void onBindViewHolder(final PostViewHolder holder, int position) {
            RessultModel postitem = postItems.get(position);
            Glide.with(getApplication()).load(postitem.getImageUrl()).asBitmap().override(150, 150).centerCrop().into(new BitmapImageViewTarget(holder.profile) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getApplication().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.profile.setImageDrawable(circularBitmapDrawable);

                }
            });
            holder.name.setText(postitem.getName());
            holder.textmessange.setText(postitem.getTextmessage());
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(postitem.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
            holder.time.setText(ago);
            holder.locname.setText(postitem.getAddress());

        }

        @Override
        public int getItemCount() {
            return  postItems.size();
        }
    }
     class PostViewHolder extends RecyclerView.ViewHolder {
        TextView name,textmessange;
        ImageView profile;
        TextView time;
        TextView locname;

        public void setName(TextView name) {
            this.name = name;
        }

        public void setTextmessange(TextView textmessange) {
            this.textmessange = textmessange;
        }

        public void setProfile(ImageView profile) {
            this.profile = profile;
        }

        public void setTime(TextView time) {
            this.time = time;
        }

        public void setLocname(TextView locname) {
            this.locname = locname;
        }

        PostViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            textmessange = (TextView) itemView.findViewById(R.id.txtStatusMsg);
            time = (TextView) itemView.findViewById(R.id.timestamp);
            profile = (ImageView) itemView.findViewById(R.id.profilePic);
            locname = (TextView) itemView.findViewById(R.id.locName);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(ShowstatusActivity.this, MapViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
