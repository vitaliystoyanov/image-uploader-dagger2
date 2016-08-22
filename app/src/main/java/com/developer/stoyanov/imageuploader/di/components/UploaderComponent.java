package com.developer.stoyanov.imageuploader.di.components;

import com.developer.stoyanov.imageuploader.di.scopes.UploaderScope;
import com.developer.stoyanov.imageuploader.service.NotificationWrapper;
import com.developer.stoyanov.imageuploader.service.UploadService;
import com.developer.stoyanov.imageuploader.ui.activities.MainActivity;

import dagger.Component;

@UploaderScope
@Component(dependencies = ServiceComponent.class)
public interface UploaderComponent {

    void inject(UploadService service);

    void inject(NotificationWrapper notificationWrapper);

}
