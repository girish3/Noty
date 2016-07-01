package com.girish.noty.views;

import com.girish.noty.data.NoteItem;

/**
 * Created by Girish on 19/06/16.
 */
public interface NoteMVPView {

    void showLoading();

    void hideLoading();

    void onNewNoteCreated(long id);

    void popView();
}
