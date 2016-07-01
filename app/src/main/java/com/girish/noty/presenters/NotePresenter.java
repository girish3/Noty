package com.girish.noty.presenters;

import android.content.Context;
import android.os.AsyncTask;

import com.girish.noty.data.DataHelper;
import com.girish.noty.data.NoteItem;
import com.girish.noty.views.NoteMVPView;

import java.util.List;

/**
 * Created by Girish on 19/06/16.
 */
public class NotePresenter implements PresenterInterface {

    private final NoteMVPView mView;
    private final Context mCtx;
    private DataHelper mDbHelper;

    public NotePresenter(NoteMVPView view, Context context) {
        mView = view;
        mCtx = context;
    }


    @Override
    public void onCreate() {
        mDbHelper = DataHelper.getInstance(mCtx);
    }

    @Override
    public void onStop() {

    }

    public void updateNote(NoteItem item) {
        UpdateNoteTask task = new UpdateNoteTask();
        mView.showLoading();
        task.execute(item);
    }

    public void deleteNote(long id) {
        mView.showLoading();
        DeleteNoteTask task = new DeleteNoteTask();
        task.execute(id);
    }

    private class UpdateNoteTask extends AsyncTask<NoteItem, Void, Void> {

        @Override
        protected Void doInBackground(NoteItem... params) {
            NoteItem item = params[0];
            if (item.getId() != -1)
                mDbHelper.updateNote(item.getId(), item.getTitle(), item.getDescription());
            else {
                long id = mDbHelper.insertNote(item.getTitle(), item.getDescription());
                mView.onNewNoteCreated(id);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mView.hideLoading();
        }
    }

    private class DeleteNoteTask extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... params) {
            long id = params[0];
            mDbHelper.deleteNote(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mView.hideLoading();
            mView.popView();
        }
    }
}
