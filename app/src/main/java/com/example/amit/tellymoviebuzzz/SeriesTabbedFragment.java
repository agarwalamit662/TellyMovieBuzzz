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

public class SeriesTabbedFragment extends Fragment {

    public static final String TAG = TabbedFragment.class.getSimpleName();
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static SeriesTabbedFragment newInstance(int sectionNumber) {

        SeriesTabbedFragment fragment = new SeriesTabbedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;

        //return new TabbedFragment();
    }

    public SeriesTabbedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_series_tabbed, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getChildFragmentManager());

        mViewPager = (ViewPager) v.findViewById(R.id.pager_1);
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

            if(position==3) {
             //   fragment = new PopularSeriesFragment();
              //  Bundle args = new Bundle();
              //  args.putInt(PopularSeriesFragment.ARG_SECTION_NUMBER_FRAG, position + 1);
              //  fragment.setArguments(args);
              //  return fragment;
            }
            if(position==2) {
                fragment = new WatchedFragment();
              // fragment = new UpcomingMoviesForecast();
               Bundle args = new Bundle();
                args.putInt(WatchedFragment.ARG_SECTION_NUMBER_FRAG, position + 1);
                fragment.setArguments(args);
                 return fragment;
            }
            if(position==1) {
                fragment = new ImdbUpcomingFragment();
                Bundle args = new Bundle();
                args.putInt(ImdbUpcomingFragment.ARG_SECTION_NUMBER_FRAG, position + 1);
                fragment.setArguments(args);
                 return fragment;
            }
            if(position==0) {
                fragment = new ImdbFragment();
                Bundle args = new Bundle();
                args.putInt(ImdbFragment.ARG_SECTION_NUMBER_FRAG, position + 1);
                fragment.setArguments(args);
                 return fragment;
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
                    return "This Month".toUpperCase(l);
                case 1:
                    return "Upcoming".toUpperCase(l);
                case 2:
                    return "Watched".toUpperCase(l);
                case 3:
                    return "Watchlist".toUpperCase(l);

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
            View rootView = inflater.inflate(R.layout.fragment_series_tabbed_content,
                    container, false);
            TextView dummyTextView = (TextView) rootView
                    .findViewById(R.id.section_label_1);
            dummyTextView.setText(Integer.toString(getArguments().getInt(
                    ARG_SECTION_NUMBER_FRAG)));
            return rootView;
        }
    }

}