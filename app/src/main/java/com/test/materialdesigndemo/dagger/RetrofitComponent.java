package com.test.materialdesigndemo.dagger;

import com.test.materialdesigndemo.ServiceHelper;
import com.test.materialdesigndemo.fragments.DetailsFragment;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Created by Pratik on 7/16/16.
 */

@Singleton
@Component(modules = {RetrofitModule.class})
public interface RetrofitComponent {

    void inject(ServiceHelper serviceHelper);

    void inject(DetailsFragment detailsFragment);
}
