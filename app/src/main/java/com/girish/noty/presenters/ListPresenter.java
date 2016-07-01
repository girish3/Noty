package com.girish.noty.presenters;

import android.content.Context;
import android.os.AsyncTask;

import com.girish.noty.data.DataHelper;
import com.girish.noty.data.NoteItem;
import com.girish.noty.views.ListMVPView;

import java.util.List;

/**
 * Created by Girish on 19/06/16.
 */
public class ListPresenter implements PresenterInterface {

    private final ListMVPView mView;
    private final Context mCtx;
    private DataHelper mDbHelper;

    public ListPresenter(ListMVPView view, Context context) {
        mView = view;
        mCtx = context;
    }

    @Override
    public void onCreate() {
        mDbHelper = DataHelper.getInstance(mCtx);
        mView.showLoading();
        GetNotesTask task = new GetNotesTask();
        task.execute();
    }

    @Override
    public void onStop() {

        // close db connection
    }

    private class GetNotesTask extends AsyncTask<Void, Void, List<NoteItem>> {

        @Override
        protected List<NoteItem> doInBackground(Void... params) {
            List<NoteItem> items = mDbHelper.getAllNotes();
            return items;
        }

        @Override
        protected void onPostExecute(List<NoteItem> noteItems) {
            mView.hideLoading();
            if (noteItems.size() == 0) mView.showEmptyView();
            else mView.showList(noteItems);
        }
    }
}
