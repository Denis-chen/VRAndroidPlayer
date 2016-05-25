package com.neointernet.neo360.util;

import com.neointernet.neo360.listener.AsyncTaskListener;

/**
 * Created by HSH on 16. 5. 24..
 */
public interface JsonManager {

    void makeJsonData(String url, AsyncTaskListener asyncTaskListener);
}
