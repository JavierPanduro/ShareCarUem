package com.aja.proyectointegrado;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Chat extends AppCompatActivity {
    private EditText mEdtRoom;
    private Button mBtnAdd;
    private ListView mLvRoom;
    private TextView mTxtUser;
    private ArrayAdapter<String> mAdapterRoom;
    private List<String> mListRoom = new ArrayList<>();
    private String mUserName;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private boolean isDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        mEdtRoom = (EditText) findViewById(R.id.chat_firebase_edt_room);
        mBtnAdd = (Button) findViewById(R.id.chat_firebase_btn_addroom);
        mLvRoom = (ListView) findViewById(R.id.chat_firebase_lv_room_chat);
        mTxtUser = (TextView) findViewById(R.id.chat_firebase_txt_user);
        mUserName = AppSetting.getUserName(this);
        mAdapterRoom = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mListRoom);
        mLvRoom.setAdapter(mAdapterRoom);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String room = mEdtRoom.getText().toString();
                if (room.equals("")){
                    Toast.makeText(Chat.this,getString(R.string.ins_ronum),Toast.LENGTH_SHORT).show();
                }else {
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put(room,"");
                    root.updateChildren(map);
                    DatabaseReference user = FirebaseDatabase.getInstance().getReference().child(room);
                    Map<String,Object> userMap = new HashMap<String, Object>();
                    userMap.put(getString(R.string.UserCreate),mUserName);
                    user.updateChildren(userMap);
                    mEdtRoom.setText("");
                }
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator item = dataSnapshot.getChildren().iterator();
                while (item.hasNext()){
                    set.add(((DataSnapshot)item.next()).getKey());
                }
                mListRoom.clear();
                mListRoom.addAll(set);
                mAdapterRoom.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mLvRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ChatRomActivity.class);
                intent.putExtra(getString(R.string.room_name),((TextView)view).getText().toString());
                intent.putExtra(getString(R.string.user_name),mUserName);
                startActivity(intent);
            }
        });

        mLvRoom.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Dialog dialog = new Dialog(Chat.this);
                Button button = new Button(Chat.this);
                button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT));
                button.setText(getString(R.string.del));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isDelete = true;
                        final String chatRoomToDelete = mAdapterRoom.getItem(i);
                        root.child(chatRoomToDelete).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (isDelete){
                                    String userKey = "";
                                    Map<String,Object> hashMap = (Map<String, Object>) dataSnapshot.getValue();
                                    try{
                                        for (String key : hashMap.keySet()) {
                                            if (key.equals(getString(R.string.UserCreate))){
                                                userKey = (String) hashMap.get(key);
                                                break;
                                            }
                                        }
                                        if (userKey.equals(mUserName)){
                                            root.child(chatRoomToDelete).removeValue(new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    if (databaseError == null) {
                                                        mAdapterRoom.remove(chatRoomToDelete);
                                                    } else {
                                                        // show databaseError to user
                                                        Toast.makeText(Chat.this,getString(R.string.err),Toast.LENGTH_SHORT).show();
                                                    }
                                                    dialog.dismiss();
                                                    isDelete = false;
                                                }
                                            });
                                        }else {
                                            Toast.makeText(Chat.this,getString(R.string.not_master),Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            isDelete = false;
                                        }
                                    }catch (NullPointerException e){
                                        dialog.dismiss();
                                        mAdapterRoom.remove(chatRoomToDelete);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
                dialog.setTitle(getString(R.string.del_room));
                dialog.setContentView(button);
                dialog.show();
                return true;
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        if (mUserName.equals(name)){
            setUser();
        }else {
            requestUser();
        }
    }

    private void requestUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String name = user.getDisplayName();
        builder.setTitle(getString(R.string.this_us)+name);
        builder.setPositiveButton(getString(R.string.ready), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mUserName = name;
                AppSetting.setUserName(Chat.this,mUserName);
                setUser();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void setUser(){
        mTxtUser.setText(mUserName);
        mTxtUser.setVisibility(View.VISIBLE);
    }
}