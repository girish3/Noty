package com.girish.noty.views;

import com.girish.noty.data.NoteItem;

import java.util.List;

/**
 * Created by Girish on 19/06/16.
 */
public interface ListMVPView {
    void showList(List<NoteItem> items);

    void showLoading();

    void hideLoading();

    void showEmptyView();
}
