package com.reiya.pixiv.historyranking;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.reiya.pixiv.base.BaseFragment;
import com.reiya.pixiv.bean.Theme;
import com.reiya.pixiv.util.StringHelper;

import java.util.Calendar;

import tech.yojigen.pivisionm.R;

public class HistoryRankingActivity extends AppCompatActivity {
    private Calendar date;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView tv;
    private HistoryRankingPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Theme.getTheme());
        setContentView(R.layout.activity_history_ranking);

        mPresenter = new HistoryRankingPresenter();

        date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) - 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24px);
        getSupportActionBar().setTitle("");
        tv = toolbar.findViewById(R.id.title);
        tv.setOnClickListener(v -> showDialog());

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        showDialog();
    }

    private void showDialog() {
        new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);
            String str = StringHelper.getFormattedDate(year, monthOfYear, dayOfMonth, "-");
            tv.setText(str);
            viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), getApplicationContext(), str));
            tabLayout.setupWithViewPager(viewPager);
        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        public final int COUNT = 3;
        private final String[] titles = new String[]{getString(R.string.daily), getString(R.string.weekly), getString(R.string.monthly)};
        private final Context context;
        private final String date;

        public MyFragmentPagerAdapter(FragmentManager fm, Context context, String date) {
            super(fm);
            if (fm.getFragments() != null) {
                fm.getFragments().clear();
            }
            this.context = context;
            this.date = date;
        }

        @Override
        public Fragment getItem(int position) {
//            Log.e("item", "" + position);
//            if (fragments[position] == null) {
//                fragments[position] = RankingFragment.newInstance(position, mode);
//            }
//            return fragments[position];
            BaseFragment fragment = HistoryRankingFragment.newInstance(position, date);
            return fragment;
        }

        @Override
        public int getCount() {
            return COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
