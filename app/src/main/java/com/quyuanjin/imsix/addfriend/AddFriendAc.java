package com.quyuanjin.imsix.addfriend;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.quyuanjin.imsix.R;
import com.quyuanjin.imsix.utils.StatusBarUtils;

public class AddFriendAc extends AppCompatActivity implements View.OnClickListener {

    private EditText searchSomething;
    private TextView search_text_view;

    private LinearLayout search_result;
    private ListView addFriendAcLV;
    private Button cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarColor(AddFriendAc.this, R.color.white);
        setContentView(R.layout.activity_add_friend);

        initView();
        initListener();
    }

    private void initListener() {
        cancel.setOnClickListener(this);
        searchSomething.setOnClickListener(this);
        searchSomething.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    addFriendAcLV.setVisibility(View.VISIBLE);
                    search_result.setVisibility(View.VISIBLE);
                    search_result.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(AddFriendAc.this, AddFriendSearchUserInfoAc.class);
                            intent.putExtra("search", search_text_view.getText().toString());
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finish();
                        }
                    });
                    search_text_view.setText(searchSomething.getText());
                } else {
                    addFriendAcLV.setVisibility(View.GONE);
                    search_result.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initView() {
        searchSomething = findViewById(R.id.search_something);
        search_result = findViewById(R.id.search_result);
        search_text_view = findViewById(R.id.keywordTextView);
        addFriendAcLV = findViewById(R.id.addFriendAcLV);
        cancel = findViewById(R.id.cancel);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_something:


                break;
            case R.id.cancel:
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

        }
    }
}
