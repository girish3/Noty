package com.girish.noty;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.girish.noty.data.NoteItem;
import com.girish.noty.interfaces.OnBackPressedListener;
import com.girish.noty.presenters.NotePresenter;
import com.girish.noty.views.NoteMVPView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment implements OnBackPressedListener, NoteMVPView {


    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESC = "desc";
    @BindView(R.id.title)
    EditText mEtTitle;
    @BindView(R.id.description)
    EditText mETDesc;
    @BindView(R.id.edit_button)
    ImageView mEditButton;
    private boolean mIsNew = false;
    private String mTitle;
    private String mDesc;
    private long mId = -1;
    private ContainerActivity mParentActivity;
    private boolean mIsEditMode;
    private NotePresenter mPresenter;
    private boolean mDeleteClick = false;

    public NoteFragment() {
        // Required empty public constructor
    }

    public static NoteFragment newInstance() {
        NoteFragment fragment = new NoteFragment();
        return fragment;
    }

    public static NoteFragment newInstance(long id, String title, String desc) {
        NoteFragment fragment = new NoteFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ID, id);
        bundle.putString(TITLE, title);
        bundle.putString(DESC, desc);
        fragment.setArguments(bundle);
        return fragment;
    }

    @OnClick(R.id.edit_button)
    public void onEditClick() {
        setEditMode(true);
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

        Bundle bundle = getArguments();
        if (bundle != null) {
            mIsNew = false;
            mTitle = bundle.getString(TITLE);
            mDesc = bundle.getString(DESC);
            mId = bundle.getLong(ID);
        } else mIsNew = true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        init();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_action:
                mDeleteClick = true;
                deleteNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteNote() {
        if (mId != -1) {
            mPresenter.deleteNote(mId);
        } else {
            mParentActivity.pop();
        }
    }

    private void init() {

        mParentActivity.displayUpButton(true);
        mPresenter = new NotePresenter(this, getActivity());
        mPresenter.onCreate();

        if (mIsNew) {
            setEditMode(true);
        } else {
            mEtTitle.setText(mTitle);
            mETDesc.setText(mDesc);
            setEditMode(false);
        }

        mDeleteClick = false;
    }

    private void setEditMode(boolean isEditable) {
        if (isEditable) {
            mIsEditMode = true;
            mEtTitle.setEnabled(true);
            mETDesc.setEnabled(true);
            mETDesc.setSelection(mETDesc.getText().length());
            mETDesc.requestFocus();
            mParentActivity.showKeyboard();
            mEditButton.setVisibility(View.GONE);
        } else {
            mIsEditMode = false;
            mEtTitle.setEnabled(false);
            mETDesc.setEnabled(false);
            mEditButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        updateNote();
    }

    private boolean updateNote() {

        if (mDeleteClick) return false;

        if (mIsEditMode) {

            // filling data object
            NoteItem item = new NoteItem();
            item.setTitle(mEtTitle.getText().toString());
            item.setDescription(mETDesc.getText().toString());
            item.setId(mId);

            // empty note is not updated
            if (mId == -1 && item.getTitle().trim().equals("") && item.getDescription().trim().equals("")) {
                return false;
            }

            // updating note
            mPresenter.updateNote(item);

            setEditMode(false);
            return true;
        } else return false;
    }

    @Override
    public boolean onBackPressed() {
        return updateNote();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mParentActivity.getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onNewNoteCreated(long id) {
        mId = id;
    }

    @Override
    public void popView() {
        mParentActivity.pop();
    }
}
