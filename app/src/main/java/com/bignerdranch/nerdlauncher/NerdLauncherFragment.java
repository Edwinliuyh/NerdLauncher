package com.bignerdranch.nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by dell on 2016/3/20.
 */
public class NerdLauncherFragment extends ListFragment{
    private static final String TAG="NerdLauncherFragment";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //所有应用的主activity都会响应的隐式intent，这两个参数就是intent-filter的
        Intent startupIntent= new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        //PackageManager返回查询到的ResolveInfo List
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);

        Log.i(TAG, "I've found" + activities.size() + "activities.");

        // 按字母顺序对activity进行排序
        // LabelName通常也就是应用名，使用ResolveInfo.loadLabel(...)方法
        Collections.sort(activities, new Comparator<ResolveInfo>(){
            public int compare(ResolveInfo a, ResolveInfo b){
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(
                        a.loadLabel(pm).toString(),
                        b.loadLabel(pm).toString());
            }
        });

        // 创建一个适配器，显示List中每一个ResolveInfo的Label
        ArrayAdapter <ResolveInfo> adapter = new ArrayAdapter<ResolveInfo>(
            getActivity(), android.R.layout.simple_list_item_1, activities){
                public View getView(int pos, View convertView, ViewGroup parent){
                    PackageManager pm= getActivity().getPackageManager();
                    View v = super.getView(pos, convertView, parent);
                    TextView tv= (TextView) v;

                    ResolveInfo ri = getItem(pos);
                    tv.setText(ri.loadLabel(pm));
                    return v;
                }
            };
            setListAdapter(adapter);
    }

    /**
     * 通过ResolveInfo获得ActivityInfo，使用ActivityInfo对象中的数据信息，启动目标activity
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        ResolveInfo resolveInfo= (ResolveInfo) l.getAdapter().getItem(position);
        ActivityInfo activityInfo=resolveInfo.activityInfo;

        if(activityInfo==null) return;

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在新任务中启动

        startActivity(i);
    }
}
