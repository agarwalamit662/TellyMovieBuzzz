package com.example.amit.tellymoviebuzzz;

/**
 * Created by Amit on 13-06-2015.
 */
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabbedFragment extends Fragment {

    public static final String TAG = TabbedFragment.class.getSimpleName();
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static TabbedFragment newInstance(int sectionNumber) {

        TabbedFragment fragment = new TabbedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;

        //return new TabbedFragment();
    }

    public TabbedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tabbed, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getChildFragmentManager());

        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        return v;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new Fragment();

            if(position==0) {
             //   fragment = new UpcomingMoviesForecast();
             //   Bundle args = new Bundle();
             //   args.putInt(UpcomingMoviesForecast.ARG_SECTION_NUMBER_FRAG, position + 1);
             //   fragment.setArguments(args);
             //   return fragment;
            }
            if(position==1) {
          ///      fragment = new TabbedContentFragment();
            //    fragment = new PopularFragment();
            //   Bundle args = new Bundle();
            //    args.putInt(PopularFragment.ARG_SECTION_NUMBER_FRAG, position + 1);
            //    fragment.setArguments(args);
            //    return fragment;
            }
            if(position==2) {
             //   fragment = new WatchlistMovieForecast();
             //   Bundle args = new Bundle();
             //   args.putInt(WatchlistMovieForecast.ARG_SECTION_NUMBER_FRAG, position + 1);
             //   fragment.setArguments(args);
             //   return fragment;
            }
            if(position==3) {
             //   fragment = new ThisYearFragment();
             //   Bundle args = new Bundle();
             //   args.putInt(ThisYearFragment.ARG_SECTION_NUMBER_FRAG, position + 1);
             //   fragment.setArguments(args);
             //   return fragment;
            }


            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section5).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section6).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section7).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section8).toUpperCase(l);

            }
            return null;
        }
    }

    public static class TabbedContentFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER_FRAG = "section_number_tag";

        public TabbedContentFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tabbed_content,
                    container, false);
            TextView dummyTextView = (TextView) rootView
                    .findViewById(R.id.section_label);
            dummyTextView.setText(Integer.toString(getArguments().getInt(
                    ARG_SECTION_NUMBER_FRAG)));
            return rootView;
        }
    }

}