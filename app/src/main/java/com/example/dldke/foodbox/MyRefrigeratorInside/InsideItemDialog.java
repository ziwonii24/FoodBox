package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.dldke.foodbox.HalfRecipe.DCItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class InsideItemDialog extends Dialog implements View.OnClickListener {

    private TextView txtName, txtOk;
    private RecyclerView recyclerView;
    private Context context;
    private String mName;

    private RecyclerView.Adapter adapter;
    private ArrayList<DCItem> dcArray = new ArrayList<>();
    //private ArrayList<DCItem> mItems = new ArrayList<>();

    public InsideItemDialog(@NonNull Context context, String name, ArrayList<DCItem> dcArray) {
        super(context);
        this.context = context;
        this.mName = name;
        this.dcArray = dcArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refrigeratorinside_item_dialog);

        txtName = (TextView) findViewById(R.id.txt_name);
        txtOk = (TextView) findViewById(R.id.txt_ok);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        txtOk.setOnClickListener(this);
        txtName.setText(mName);

        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new InsideItemAdapter(dcArray);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, new LinearLayoutManager(context).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        //setData();
    }

//    private void setData() {
//        mItems.clear();
//
//        for (int i = 0; i < dcArray.size(); i++) {
//            mItems.add(new DCItem(dcArray.get(i).getStrDueDate(), dcArray.get(i).getCount()));
//        }
//
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_ok:
                dismiss();
                break;
        }
    }
}
