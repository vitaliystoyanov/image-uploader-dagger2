package com.developer.stoyanov.imageuploader.di.components;

import com.developer.stoyanov.imageuploader.di.modules.BroadcastModule;
import com.developer.stoyanov.imageuploader.di.scopes.FragmentScope;
import com.developer.stoyanov.imageuploader.ui.fragments.GetStartedFragment;
import com.developer.stoyanov.imageuploader.ui.fragments.ProcessFragment;
import com.developer.stoyanov.imageuploader.ui.fragments.ResponseFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = ActivityComponent.class, modules = BroadcastModule.class)
public interface FragmentComponent {

    void inject(GetStartedFragment fragment);

    void inject(ResponseFragment fragment);

    void inject(ProcessFragment fragment);

}
