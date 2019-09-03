package com.dimfcompany.signpdfapp.base;

import android.app.Application;
import android.util.Log;

import com.dimfcompany.signpdfapp.di.application.ApplicationComponent;
import com.dimfcompany.signpdfapp.di.application.ApplicationModule;
import com.dimfcompany.signpdfapp.di.application.DaggerApplicationComponent;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AppClass extends Application
{
    ApplicationComponent applicationComponent;
    private static AppClass app;

    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        int[] nums = new int[]{1,1,2,2,2,4,4,4,7,7,7,9,9,9};

        int answer = removeDuplicates(nums);
        Log.e(TAG, "removeDuplicates: Answer is "+answer );
    }

    public static AppClass getApp()
    {
        return app;
    }

    public ApplicationComponent getApplicationComponent()
    {
        return applicationComponent;
    }

    public int removeDuplicates(int[] nums)
    {
        int i = 0;

        for(int j =0;j<nums.length;j++)
        {
            if(nums[i] != nums[j])
            {
                i++;
                nums[i] = nums[j];
            }
        }

        for(int num : nums)
        {
            Log.e(TAG, "***** Num is "+num+" *****" );
        }

        return i+1;
    }
}
