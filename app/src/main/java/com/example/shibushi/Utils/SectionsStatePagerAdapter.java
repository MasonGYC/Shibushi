package com.example.shibushi.Utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SectionsStatePagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final HashMap<Fragment, Integer> mFragments = new HashMap<>(); // Get fragment number from Fragment
    private final HashMap<String, Integer> mFragmentNumbers = new HashMap<>(); // Get fragment number from fragment name
    private final HashMap<Integer, String> mFragmentNames = new HashMap<>(); // Get fragment name from fragment number

    public SectionsStatePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }


    /**
     * fill up mFragmentList, mFragments, mFragmentNumbers and mFragmentNames
     * @param fragment
     * @param fragmentName
     */
    public void addFragment(Fragment fragment, String fragmentName) {
        mFragmentList.add(fragment);
        int fragmentNum = mFragmentList.size()-1;
        mFragments.put(fragment, mFragmentList.size()-1);
        mFragmentNumbers.put(fragmentName, fragmentNum);
        mFragmentNames.put(fragmentNum, fragmentName);
    }

    /**
     * returns the fragmentNumber from fragmentName
     * @param fragmentName
     * @return fragmentNumber
     */
    public Integer getFragmentNumber(String fragmentName){
        if(mFragmentNumbers.containsKey(fragmentName)){
            return mFragmentNumbers.get(fragmentName);
        }else{
            return null;
        }
    }


    /**
     * returns the fragmentNumber from fragment
     * @param fragment
     * @return fragmentNumber
     */
    public Integer getFragmentNumber(Fragment fragment){
        if(mFragmentNumbers.containsKey(fragment)){
            return mFragmentNumbers.get(fragment);
        }else{
            return null;
        }
    }

    /**
     * returns the fragment name from fragmentNumber
     * @param fragmentNumber
     * @return fragmentName
     */
    public String getFragmentName(Integer fragmentNumber){
        if(mFragmentNames.containsKey(fragmentNumber)){
            return mFragmentNames.get(fragmentNumber);
        }else{
            return null;
        }
    }
}
