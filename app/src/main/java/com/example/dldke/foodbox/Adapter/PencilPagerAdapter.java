package com.example.dldke.foodbox.Adapter;

import android.content.Context;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.example.dldke.foodbox.Fragments.AllFoodListFragment;
import com.example.dldke.foodbox.Fragments.EtcListFragment;
import com.example.dldke.foodbox.Fragments.FreshListFragment;
import com.example.dldke.foodbox.Fragments.MeatListFragment;
import com.example.dldke.foodbox.Fragments.SideListFragment;
import com.example.dldke.foodbox.R;


public class PencilPagerAdapter extends FragmentStatePagerAdapter {

    public PencilPagerAdapter(android.support.v4.app.FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public android.support.v4.app.Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return new AllFoodListFragment();
            case 1:
                return new MeatListFragment();
            case 2:
                return new FreshListFragment();
            case 3:
                return new EtcListFragment();
            case 4:
                return new SideListFragment();
            default:
                return null;
        }
    }
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch(position)
        {
            case 0:
                return "전체";
            case 1:
                return "육류/수산물";
            case 2:
                return "과일/채소";
            case 3:
                return "음료/유제품";
            case 4:
                return "반찬";
            default:
                return null;
        }
    }
    @Override
    public int getCount()
    {
        return 5;
    }

}


