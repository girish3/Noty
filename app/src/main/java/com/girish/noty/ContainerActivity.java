package com.girish.noty;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.girish.noty.interfaces.OnBackPressedListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContainerActivity extends AppCompatActivity {

    @BindView(R.id.progress_view)
    RelativeLayout mProgressView;

    FragmentTransaction mFragmenTransaction;
    boolean mDoublePressFlag = false;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mFragmentManager = getSupportFragmentManager();
        addFragment(ListFragment.newInstance());
    }

    public void addFragment(Fragment fragment) {
        mFragmenTransaction = mFragmentManager.beginTransaction();
        mFragmenTransaction
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public Fragment getTopFragment() {

        if (mFragmentManager != null) {
            Fragment fragment = mFragmentManager
                    .findFragmentById(R.id.fragment_container);
            return fragment;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getTopFragment();

        if (fragment != null && fragment instanceof OnBackPressedListener) {
            boolean isConsumed = ((OnBackPressedListener) fragment).onBackPressed();
            if (isConsumed) return;
        }

        if (mFragmentManager.getBackStackEntryCount() == 1) {
            if (mDoublePressFlag) {
                finish();
            } else {
                mDoublePressFlag = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mDoublePressFlag = false;
                    }
                }, 2000);
            }
            return;
        }
        super.onBackPressed();
    }

    public void showProgressBar() {
        mProgressView.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        mProgressView.setVisibility(View.GONE);
    }

    public void pop() {
        if (mFragmentManager.getBackStackEntryCount() > 0)
            mFragmentManager.popBackStackImmediate();
    }

    @Override
    public boolean onSupportNavigateUp() {
        mFragmentManager.popBackStack();
        return true;
    }

    public void displayUpButton(boolean b) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(b);
    }

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
