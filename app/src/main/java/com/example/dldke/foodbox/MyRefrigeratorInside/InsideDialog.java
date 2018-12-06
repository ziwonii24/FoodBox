package com.example.dldke.foodbox.MyRefrigeratorInside;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dldke.foodbox.HalfRecipe.DCItem;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeIngreItem;
import com.example.dldke.foodbox.HalfRecipe.HalfRecipeRecyclerListener;
import com.example.dldke.foodbox.HalfRecipe.LocalRefrigeratorItem;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;

public class InsideDialog extends Dialog implements View.OnClickListener {

    private TextView txtType, txtEmpty;
    private Button btnBackEmpty, btnCancel, btnOk;
    private LinearLayout linearLayout1, linearLayout2;
    private RecyclerView recyclerView;

    private Context context;
    private String ingreType;
    private boolean isEmpty;

    private RecyclerView.Adapter adapter;
    private ArrayList<String> nameArray = new ArrayList<>();
    private ArrayList<HalfRecipeIngreItem> mItems = new ArrayList<>();

    private ArrayList<LocalRefrigeratorItem> localArray = new ArrayList<>();
    private ArrayList<DCItem> dcArray = new ArrayList<>();

    private InsideItemDialog itemDialog;


    public InsideDialog(@NonNull Context context, String type, boolean isEmpty) {
        super(context);
        this.context = context;
        this.ingreType = type;
        this.isEmpty = isEmpty;
    }

    public InsideDialog(@NonNull Context context, String type, boolean isEmpty, ArrayList<LocalRefrigeratorItem> localArray, ArrayList<String> nameArray) {
        super(context);
        this.context = context;
        this.ingreType = type;
        this.isEmpty = isEmpty;
        this.localArray = localArray;
        this.nameArray = nameArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.halfrecipe_ingredient_dialog);

        Log.e("test", "create하자마자 localArray size: "+localArray.size());
        for (int i=0; i<localArray.size(); i++) {
            Log.d("test", i+ " : "+localArray.get(i).getName()+", "+localArray.get(i).getDueDate()+", "+localArray.get(i).getCount());
        }

        txtType = (TextView) findViewById(R.id.txt_type);
        txtEmpty = (TextView) findViewById(R.id.txt_empty);
        btnBackEmpty = (Button) findViewById(R.id.btn_back_empty);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk = (Button) findViewById(R.id.btn_ok);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayout1 = (LinearLayout) findViewById(R.id.layout1);
        linearLayout2 = (LinearLayout) findViewById(R.id.layout2);

        btnCancel.setVisibility(View.INVISIBLE);
        btnBackEmpty.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        switch (ingreType) {
            case "sideDish":
                txtType.setText("반찬칸");
                break;
            case "dairy":
                txtType.setText("계란,유제품칸");
                break;
            case "etc":
                txtType.setText("음료,소스칸");
                break;
            case "meat":
                txtType.setText("육류,생선칸");
                break;
            case "fresh":
                txtType.setText("과일,야채칸");
                break;
        }

        if (isEmpty) {
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.GONE);
            setRecyclerView();
        }
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new InsideAdapter(mItems, ingreType);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        recyclerView.addOnItemTouchListener(
                new HalfRecipeRecyclerListener(context, recyclerView, new HalfRecipeRecyclerListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        Log.d("test", "setRecyclerView에서 클릭했을때");

                        Log.e("test", "setRecyclerView에서 클릭했을때 localArray size: "+localArray.size());
                        for (int i=0; i<localArray.size(); i++) {
                            Log.d("test", i+ " : "+localArray.get(i).getName()+", "+localArray.get(i).getDueDate()+", "+localArray.get(i).getCount());
                        }

                        showItemDialog(position);
                    }
                }
                ));

        setData();
    }

    private void setData() {
        mItems.clear();

        for (int i = 0; i < nameArray.size(); i++) {
            mItems.add(new HalfRecipeIngreItem(nameArray.get(i)));
        }

        adapter.notifyDataSetChanged();
    }

    public void showItemDialog(final int position) {
        dcArray.clear();
        for (int i=0; i<localArray.size(); i++) {
            if (localArray.get(i).getName().equals(nameArray.get(position)))
                dcArray.add(new DCItem(localArray.get(i).getDueDate(), localArray.get(i).getCount()));
        }

        Log.e("test", "재료 클릭했을때 상세보기다이얼로그 보여질때 dcArray size: "+dcArray.size());
        for (int i=0; i<dcArray.size(); i++) {
            Log.d("test", i+","+dcArray.get(i).getStrDueDate()+", "+dcArray.get(i).getCount());
        }

        itemDialog = new InsideItemDialog(context, nameArray.get(position), dcArray);
        itemDialog.setDialogListener(new InsideDialogListener() {
            @Override
            public void onPositiveClicked(int delCheck, Double count, String dueDate) { }

            @Override
            public void onOkClicked(ArrayList<DCItem> dcItems) {
                Log.e("test", "오케이버튼 클릭하고 어댑터에서 받아오는 (수정/삭제 후) 유통기한리스트");
                for (int i=0; i<dcItems.size(); i++) {
                    Log.d("test", i+","+dcItems.get(i).getStrDueDate() + ", " + dcItems.get(i).getCount());
                }

                Log.e("test", "재료이름 찾아서 삭제 하기 전 localArray size: "+localArray.size());
                for (int i=0; i<localArray.size(); i++) {
                    Log.d("test", i+ " : "+localArray.get(i).getName()+", "+localArray.get(i).getDueDate()+", "+localArray.get(i).getCount());
                }

                int cnt = 0;
                Log.e("test", "nameArray.get(position): "+nameArray.get(position));
                for (int i=0; i<localArray.size(); i++) {
                    if (localArray.get(i).getName().equals(nameArray.get(position))){
                        cnt++;
                    }
                }

                for (int i=0; i<cnt; i++)
                    localArray.remove(new LocalRefrigeratorItem(nameArray.get(position)));

                Log.e("test", "재료이름 찾아서 삭제 하고 localArray size: "+localArray.size());
                for (int i=0; i<localArray.size(); i++) {
                    Log.d("test", i+ " : "+localArray.get(i).getName()+", "+localArray.get(i).getDueDate()+", "+localArray.get(i).getCount());
                }

                for (int i=0; i<dcItems.size(); i++) {
                    localArray.add(new LocalRefrigeratorItem(nameArray.get(position), dcItems.get(i).getCount(), dcItems.get(i).getStrDueDate()));
                }

                Log.e("test", "삭제후 변경안된 유통기한리스트는 살리기 localArray size: "+localArray.size());
                for (int i=0; i<localArray.size(); i++) {
                    Log.d("test", i+ " : "+localArray.get(i).getName()+", "+localArray.get(i).getDueDate()+", "+localArray.get(i).getCount());
                }
            }
        });
        itemDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back_empty:
                cancel();
                break;
            case R.id.btn_ok:
                dismiss();
                break;
        }
    }
}
