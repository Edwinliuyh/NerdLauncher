package com.bignerdranch.nerdlauncher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * 抽象类，通用的托管单一Fragment的Activity模板
 */
public abstract class SingleFragmentActivity extends FragmentActivity {

    /**
     * 抽象类的子类必须要实现抽象类中未完成的方法
     */
    protected abstract Fragment createFragment();

    /**
     * 返回activity需要的布局资源ID
     * 子类可以选择覆盖getLayoutResId()方法返回所需布局，
     */
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    /**
     * 在OnCreat，生成空布局的fragment作为Container
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());//增加布局的灵活性
        FragmentManager fm=getSupportFragmentManager();
        Fragment fragment =fm.findFragmentById(R.id.fragmentContainer);

        if(fragment==null){
            fragment=createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }


}
