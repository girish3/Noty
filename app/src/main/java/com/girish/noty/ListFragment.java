package com.girish.noty;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.girish.noty.data.NoteItem;
import com.girish.noty.presenters.ListPresenter;
import com.girish.noty.views.ListMVPView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment implements ListMVPView, View.OnClickListener {

    @BindView(R.id.empty_view)
    RelativeLayout mEmptyView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ListPresenter mPresenter;
    private ContainerActivity mParentActivity;
    private ListAdapter mAdapter;
    private List<NoteItem> mItems;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @OnClick(R.id.add_button)
    public void onAddClick() {
        Fragment fragment = NoteFragment.newInstance();
        mParentActivity.addFragment(fragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mParentActivity = (ContainerActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "cannot be cast to ContainerActivity class");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mParentActivity.displayUpButton(false);
        mPresenter = new ListPresenter(this, getActivity());
        mPresenter.onCreate();
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(mParentActivity);
        mRecyclerView.setLayoutManager(manager);
    }

    @Override
    public void showList(List<NoteItem> items) {
        mItems = items;
        mAdapter = new ListAdapter(items, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showLoading() {
        mParentActivity.showProgressBar();
    }

    @Override
    public void hideLoading() {
        mParentActivity.hideProgressBar();
    }

    @Override
    public void showEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(v);
        NoteItem item = mItems.get(itemPosition);
        Fragment fragment = NoteFragment.newInstance(item.getId(), item.getTitle(), item.getDescription());
        mParentActivity.addFragment(fragment);
    }
}
