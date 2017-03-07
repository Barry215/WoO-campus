package com.example.barry215.woo.Main.Activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.barry215.woo.Adapter.FindListAdapter;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.R;
import com.example.barry215.woo.ui.DividerLine;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by barry215 on 2016/4/18.
 */
public class FindActivity extends BaseSwipeBackActivity {

    @Bind(R.id.recycler_view_find) RecyclerView recyclerView;
    @Bind(R.id.toolbar_find) Toolbar toolbar;

    private FindListAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_find;
    }

    @Override
    protected void afterCreate() {
        initToolbar();
        initViewPager();
        initData();
    }

    private void initData() {
        List<String> tabNames = new ArrayList<>();
        tabNames.add("快递");
        tabNames.add("提问");
        tabNames.add("课程");
        tabNames.add("带饭");
        tabNames.add("组队");
        tabNames.add("考试");
        tabNames.add("租借");
        tabNames.add("寻找");
        tabNames.add("电脑");
        tabNames.add("求偶");
        tabNames.add("买卖");
        tabNames.add("运动");
        tabNames.add("跑路");
        tabNames.add("游戏");
        tabNames.add("其他");

        List<String> tabInfo = new ArrayList<>();
        tabInfo.add("可以召唤小伙伴去快递哟");
        tabInfo.add("大学里大神这么多，快来提问把");
        tabInfo.add("选课又没选到如意的，还不快来这里找找机会");
        tabInfo.add("不想下寝室啊，看来又要找人带饭了");
        tabInfo.add("竞赛组队，上课也组队，我不想落单啊");
        tabInfo.add("考试范围？就我傻傻地不知道？");
        tabInfo.add("太贵买不起，想租却找不到地方...");
        tabInfo.add("众里寻他千百度，未果，上墙上墙");
        tabInfo.add("只求一个有技术的人，花钱也在所不惜");
        tabInfo.add("茫茫人海，我怎么才能见到那个人");
        tabInfo.add("快毕业了，一堆东西带不走啊，卖了...");
        tabInfo.add("本人精通这种球，就是缺个对手");
        tabInfo.add("最近太忙了，这事得托人去办啊");
        tabInfo.add("五黑四缺一，麻将三缺一，怎么老是差一个啊");
        tabInfo.add("大学生活丰富多彩，怎么能缺了我呢");

        List<Integer> tabImage = new ArrayList<>();
        tabImage.add(R.drawable.express);
        tabImage.add(R.drawable.question);
        tabImage.add(R.drawable.classes);
        tabImage.add(R.drawable.food);
        tabImage.add(R.drawable.group);
        tabImage.add(R.drawable.exam);
        tabImage.add(R.drawable.rent);
        tabImage.add(R.drawable.broadcast);
        tabImage.add(R.drawable.reward);
        tabImage.add(R.drawable.love);
        tabImage.add(R.drawable.sold);
        tabImage.add(R.drawable.sports);
        tabImage.add(R.drawable.runaway);
        tabImage.add(R.drawable.game);
        tabImage.add(R.drawable.other);

        adapter.addAll(tabNames,tabInfo,tabImage);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        setTitle("");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViewPager() {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new FindListAdapter(FindActivity.this);
        recyclerView.setAdapter(adapter);
        DividerLine dividerLine = new DividerLine(DividerLine.VERTICAL);
        dividerLine.setSize(1);
        dividerLine.setColor(getResources().getColor(R.color.SUB_GRAY));
        recyclerView.addItemDecoration(dividerLine);
    }
}
