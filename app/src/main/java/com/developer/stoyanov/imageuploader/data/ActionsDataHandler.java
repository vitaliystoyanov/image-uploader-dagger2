package com.developer.stoyanov.imageuploader.data;

import android.os.Bundle;

public interface ActionsDataHandler {

    void setStatement(int statement);

    int getStatement();

    void setData(Bundle bundle);

    Bundle getData();

    void setLanguage(String lang);

    String getLanguage();

    void setDefaultLanguage(String lang);

    String getDefaultLanguage();
}
