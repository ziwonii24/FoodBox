package com.example.dldke.foodbox.Activity;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.dldke.foodbox.R;


public class RefrigeratorMainActivity extends AppCompatActivity {


    private static final int LAYOUT = R.layout.activity_refrigerator;


    /*********************FloatingButtons***********************/
    //플로팅 버튼 애니메이션
    Animation ShowPlus, HidePlus,LayHide,ShowMinus,HideMinus, LayShow;
    //플로팅 버튼
    private FloatingActionButton fabPlus, fabCamera, fabPencil, fabMinus, fabFull, fabMini;
    //플로팅 레이아웃 (아이콘 + 아이콘 글씨)
    LinearLayout CameraLayout, PencilLayout, FullLayout, MiniLayout;
    //플로팅 배경 레이아웃
    RelativeLayout plusBack, minusBack;


    /***********************Menu****************************/
    //메뉴 버튼
    ImageView menu;
    //메뉴 배경 레이아웃
    LinearLayout menuTransBack;
    //메뉴창에 들어갈 리스트
    static final String[] LIST_MENU = {"내 레시피 보기", "Community", "Store","로그아웃"} ;
    //메뉴 슬라이딩 열기/닫기 플래그
    boolean isPageOpen = false;
    //메뉴 슬라이드 열기/닫기 애니메이션
    Animation leftAnim, rightAnim;
    //메뉴 슬라이드 레이아웃
    LinearLayout menuPage;
    //메뉴창 리스트뷰
    ListView listview;

    /***********************Refrigerator****************************/
    //냉장고 오른쪽/왼쪽 부분
    Button leftDoor;
    Button rightDoor;


    /***************************etc********************************/
    ImageView postit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);




        /*메뉴*/
        menuTransBack = (LinearLayout)findViewById(R.id.transparentBack);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;
        menu = (ImageView)findViewById(R.id.menu);
        listview = (ListView) findViewById(R.id.optionList) ;
        listview.setAdapter(adapter) ;
        menuPage = (LinearLayout)findViewById(R.id.optionPage);
        leftAnim = AnimationUtils.loadAnimation(this, R.anim.go_left);
        rightAnim = AnimationUtils.loadAnimation(this, R.anim.go_right);
        //메뉴 애니메이션 이벤트리스너
        SlidingPageAnimationListener animationListener = new SlidingPageAnimationListener();
        leftAnim.setAnimationListener(animationListener);
        rightAnim.setAnimationListener(animationListener);



        /*플로팅 버튼*/
        plusBack = (RelativeLayout)findViewById(R.id.plusLayout);
        minusBack=(RelativeLayout)findViewById(R.id.minusLayout);

        fabPlus = (FloatingActionButton) findViewById(R.id.fabPlus);
        fabCamera = (FloatingActionButton) findViewById(R.id.fabCamera);
        fabPencil = (FloatingActionButton) findViewById(R.id.fabPencil);

        CameraLayout = (LinearLayout) findViewById(R.id.cameraLayout);
        PencilLayout = (LinearLayout) findViewById(R.id.pencilLayout);

        fabMinus = (FloatingActionButton) findViewById(R.id.fabMinus);
        fabFull = (FloatingActionButton) findViewById(R.id.fabFull);
        fabMini = (FloatingActionButton) findViewById(R.id.fabMini);

        FullLayout = (LinearLayout) findViewById(R.id.fullLayout);
        MiniLayout = (LinearLayout) findViewById(R.id.miniLayout);

        //플로팅 애니메이션
        ShowPlus= AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.show_button);
        HidePlus = AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.hide_button);
        ShowMinus= AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.show_minus_button);
        HideMinus = AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.hide_minus_button);
        LayShow = AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.show_layout);
        LayHide = AnimationUtils.loadAnimation(
                RefrigeratorMainActivity.this, R.anim.hide_layout);



        /*etc 버튼들*/
        leftDoor = (Button) findViewById(R.id.leftButton);
        rightDoor = (Button) findViewById(R.id.rightButton);
        postit = (ImageView)findViewById(R.id.postit);



        BtnOnClickListener onClickListener = new BtnOnClickListener() ;
        ListClickListener listClickListener = new ListClickListener();

        /************버튼 리스너들 시작**************/


        fabPlus.setOnClickListener(onClickListener);
        plusBack.setOnClickListener(onClickListener);
        fabCamera.setOnClickListener(onClickListener);
        fabPencil.setOnClickListener(onClickListener);

        fabMinus.setOnClickListener(onClickListener);
        minusBack.setOnClickListener(onClickListener);
        fabFull.setOnClickListener(onClickListener);
        fabMini.setOnClickListener(onClickListener);

        rightDoor.setOnClickListener(onClickListener);
        leftDoor.setOnClickListener(onClickListener);


        menu.setOnClickListener(onClickListener);
        menuTransBack.setOnClickListener(onClickListener);
        listview.setOnItemClickListener(listClickListener);

        postit.setOnClickListener(onClickListener);




    }


    /*************리스트뷰 리스너************/
    class ListClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView parent, View v, int position, long id)
        {
            //클릭한 리스트의 text가져오기
            String strText = (String) parent.getItemAtPosition(position) ;

            if(strText.equals("로그아웃")){
                Intent MainActivity = new Intent(getApplicationContext(), MainActivity.class);
                //로그아웃 후, 뒤로가기 누르면 다시 로그인 된 상태로 가는 것을 방지
                MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(MainActivity);
            }
            Toast.makeText(RefrigeratorMainActivity.this, strText+"눌렸어용", Toast.LENGTH_SHORT).show();
        }

    }

    /******************메뉴버튼 애니메이션 리스너***************/
    class SlidingPageAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationEnd(Animation animation) {
            //슬라이드 열기->닫기
            if(isPageOpen){
                //fabPlus.setElevation(10);
                //fabMinus.setElevation(10);
                menuTransBack.setVisibility(View.GONE);
                menuPage.setVisibility(View.GONE);
                listview.setVisibility(View.GONE);
                isPageOpen = false;
            }
            //슬라이드 닫기->열기
            else
                isPageOpen = true;

        }
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
        @Override
        public void onAnimationStart(Animation animation) {

        }
    }

    /*********************버튼 리스너********************************/
    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fabPlus:
                case R.id.plusLayout:
                    //열려있으면 닫고 여러 이벤트
                    if (plusBack.getVisibility() == View.VISIBLE ) {
                        plusBack.setVisibility(View.GONE);
                        CameraLayout.setVisibility(View.GONE);
                        PencilLayout.setVisibility(View.GONE);
                        fabCamera.startAnimation(LayHide);
                        fabPencil.startAnimation(LayHide);
                        fabPlus.startAnimation(HidePlus);
                        fabMinus.setElevation(10);

                    }
                    //닫혀있으면 열고 여러 이벤트
                    else {

                        CameraLayout.setVisibility(View.VISIBLE);
                        PencilLayout.setVisibility(View.VISIBLE);
                        plusBack.setVisibility(View.VISIBLE);
                        fabCamera.startAnimation(LayShow);
                        fabPencil.startAnimation(LayShow);
                        fabPlus.startAnimation(ShowPlus);
                        fabMinus.setElevation(0);
                    }
                    break ;

                case R.id.fabMinus :
                case R.id.minusLayout:
                    //열려있으면 닫기
                    if (minusBack.getVisibility() == View.VISIBLE ) {
                        minusBack.setVisibility(View.GONE);
                        FullLayout.setVisibility(View.GONE);
                        MiniLayout.setVisibility(View.GONE);
                        fabFull.startAnimation(LayHide);
                        fabMini.startAnimation(LayHide);
                        fabMinus.startAnimation(HideMinus);
                        fabPlus.setElevation(10);
                    }
                    //닫혀있으면 열기
                    else  {
                        minusBack.setVisibility(View.VISIBLE);
                        FullLayout.setVisibility(View.VISIBLE);
                        MiniLayout.setVisibility(View.VISIBLE);
                        fabFull.startAnimation(LayShow);
                        fabMini.startAnimation(LayShow);
                        fabMinus.startAnimation(ShowMinus);
                        fabPlus.setElevation(0);
                    }
                    break ;

                case R.id.fabCamera:
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent,4321);
                    break ;
                case R.id.fabPencil:
                    Toast.makeText(RefrigeratorMainActivity.this, "직접입력 누름", Toast.LENGTH_SHORT).show();
                    Intent PencilActivity = new Intent(getApplicationContext(),PencilRecipeActivity.class);
                    startActivity(PencilActivity);
                    //다음 화면이 아래에서 올라오는 애니메이션
                    overridePendingTransition(R.anim.bottom_to_up,R.anim.up_to_bottom);
                    break ;
                case R.id.fabFull:
                    //Toast.makeText(RefrigeratorMainActivity.this, "풀 레시피 누름", Toast.LENGTH_SHORT).show();
                    Intent fullActivity = new Intent(getApplicationContext(),FullRecipeActivity.class);
                    startActivity(fullActivity);
                    //다음 화면이 아래에서 올라오는 애니메이션
                    overridePendingTransition(R.anim.bottom_to_up,R.anim.up_to_bottom);
                    break ;
                case R.id.fabMini:
                    //Toast.makeText(RefrigeratorMainActivity.this, "간이 레시피 누름", Toast.LENGTH_SHORT).show();
                    Intent halfActivity = new Intent(getApplicationContext(),HalfRecipeActivity.class);
                    startActivity(halfActivity);
                    //다음 화면이 아래에서 올라오는 애니메이션
                    overridePendingTransition(R.anim.bottom_to_up,R.anim.up_to_bottom);
                    break ;
                case R.id.rightButton:
                    Intent rightSideActivity = new Intent(getApplicationContext(), RefrigeratorInsideActivity.class);
                    startActivity(rightSideActivity);
                    break ;
                case R.id.leftButton:
                    Toast.makeText(RefrigeratorMainActivity.this, "왼쪽 도어 누름", Toast.LENGTH_SHORT).show();
                    break ;
                case R.id.menu:
                    menuPage.startAnimation(leftAnim);
                    menuPage.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.VISIBLE);
                    //fabPlus.setElevation(0);
                    //fabMinus.setElevation(0);
                    menuTransBack.setVisibility(View.VISIBLE);
                    break ;
                case R.id.transparentBack:
                    //메뉴 누른 후 뒷배경 버튼 안 먹게 하기 위함.
                    menuPage.startAnimation(rightAnim);
                    break ;
                case R.id.postit:
                    Toast.makeText(RefrigeratorMainActivity.this, "포스트잇 눌림", Toast.LENGTH_SHORT).show();
                    break ;
            }
        }
    }


    @Override public void onBackPressed() {
        if (plusBack.getVisibility() == View.VISIBLE ) {
            plusBack.setVisibility(View.GONE);
            CameraLayout.setVisibility(View.GONE);
            PencilLayout.setVisibility(View.GONE);
            fabCamera.startAnimation(LayHide);
            fabPencil.startAnimation(LayHide);
            fabPlus.startAnimation(HidePlus);
            //fabMinus.setElevation(10);
        }
        if (minusBack.getVisibility() == View.VISIBLE ) {
            minusBack.setVisibility(View.GONE);
            FullLayout.setVisibility(View.GONE);
            MiniLayout.setVisibility(View.GONE);
            fabFull.startAnimation(LayHide);
            fabMini.startAnimation(LayHide);
            fabMinus.startAnimation(HideMinus);
            //fabPlus.setElevation(10);
        }
        else
            menuPage.startAnimation(rightAnim);


    }





}