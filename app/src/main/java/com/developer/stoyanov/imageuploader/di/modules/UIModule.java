package com.developer.stoyanov.imageuploader.di.modules;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.developer.stoyanov.imageuploader.R;
import com.developer.stoyanov.imageuploader.data.DataHandler;
import com.developer.stoyanov.imageuploader.di.qualifiers.LanguageDialog;
import com.developer.stoyanov.imageuploader.di.qualifiers.RetryDialog;
import com.developer.stoyanov.imageuploader.ui.activities.MainActivity;

import java.util.Locale;

import dagger.Module;
import dagger.Provides;

@Module
public class UIModule {

    @Provides
    @LanguageDialog
    MaterialDialog provideLanguageDialog(final MainActivity activity,
                                         final DataHandler handler) {
        return new MaterialDialog.Builder(activity)
                .title(R.string.dialog_language_title)
                .items(activity.getResources().getStringArray(R.array.language_items))
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String language = null;
                        if (which == 0) {
                            language = "en";
                        } else if (which == 1) {
                            language = "ru";
                        } else if (which == 2) {
                            language = "uk";
                        }
                        if (which != -1 && language != null && !language.isEmpty()) {
                            handler.setLanguage(language);
                            Locale.setDefault(new Locale(language));
                            Configuration config = new Configuration();
                            config.locale = new Locale(language);
                            Resources resources = activity.getResources();
                            resources.updateConfiguration(config, resources.getDisplayMetrics());
                            activity.recreate();
                        }
                        return true;
                    }
                })
                .positiveText(activity.getString(R.string.dialog_button_choose))
                .build();
    }

    @Provides
    @RetryDialog
    MaterialDialog provideRetryDialog(final MainActivity activity) {
        return new MaterialDialog.Builder(activity)
                .title(R.string.dialog_retry_title)
                .positiveText(R.string.dialog_retry_positive_button)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .content(R.string.dialog_retry_content)
                .build();
    }
}
