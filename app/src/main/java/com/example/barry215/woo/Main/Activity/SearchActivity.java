package com.example.barry215.woo.Main.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.barry215.woo.Adapter.FragmentAdapter;
import com.example.barry215.woo.Base.BaseSwipeBackActivity;
import com.example.barry215.woo.Main.Fragment.SearchContentFragment;
import com.example.barry215.woo.Main.Fragment.SearchPersonFragment;
import com.example.barry215.woo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by barry215 on 2016/4/12.
 */
public class SearchActivity extends BaseSwipeBackActivity {

    @Bind(R.id.tabs_search) TabLayout tabLayout;
    @Bind(R.id.viewPager_search) ViewPager viewPager;
    @Bind(R.id.floating_search_view) FloatingSearchView floatingSearchView;

    private static final int REFRESH_DELAY = 1000;
    private OnTextSearchListener mOnTextSearchListener;
    private OnPersonSearchListener mOnPersonSearchListener;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    public interface OnTextSearchListener {
        void onTextSearch(String newText);
    }

    public interface OnPersonSearchListener{
        void OnPersonSearch(String newText);
    }

    public void setOnTextSearchListener(OnTextSearchListener mOnTextSearchListener) {
        this.mOnTextSearchListener = mOnTextSearchListener;
    }

    public void setOnPersonSearchListener(OnPersonSearchListener mOnPersonSearchListener) {
        this.mOnPersonSearchListener = mOnPersonSearchListener;
    }

    @Override
    protected void afterCreate() {
        initViewPager();
        initListener();
    }

    private void initListener() {
        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    floatingSearchView.clearSuggestions();
                }else {
                    floatingSearchView.showProgress();
                    floatingSearchView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            floatingSearchView.hideProgress();
                        }
                    }, REFRESH_DELAY);

                    mOnTextSearchListener.onTextSearch(newQuery);
                    mOnPersonSearchListener.OnPersonSearch(newQuery);
                }
            }
        });

        floatingSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                finish();
            }
        });
    }

    private void initViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("内容");
        titles.add("人");
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new SearchContentFragment());
        fragments.add(new SearchPersonFragment());
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}
