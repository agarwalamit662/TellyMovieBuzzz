package com.example.amit.tellymoviebuzzz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

/**
 * Created by Amit on 23-06-2015.
 */
public class LogOutFragment extends Fragment {

    private static final int LOGIN_REQUEST = 0;

    private TextView titleTextView;
    private TextView emailTextView;
    private TextView nameTextView;
    private Button loginOrLogoutButton;

    private TextView txtlogout ;
    private Button logoutbutton;

    private ParseUser currentUser;


    private static final String ARG_SECTION_NUMBER = "section_number";



    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LogOutFragment newInstance(int sectionNumber) {
        LogOutFragment fragment = new LogOutFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public LogOutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.logout_profile, container, false);



        txtlogout = (TextView) rootView.findViewById(R.id.textView_log_out);

        logoutbutton = (Button) rootView.findViewById(R.id.button_log_out);

        txtlogout.setText("Log Out From TellyMovieBuzz");
        logoutbutton.setText("Log Out");
        currentUser = ParseUser.getCurrentUser();

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser != null) {
                    // User clicked to log out.
                    ParseUser.logOut();
                    currentUser = null;
                   // SampleProfileActivity.showProfileLoggedOut();

                    Intent intent = new Intent(getActivity(),SampleProfileActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });



        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}


