package com.medi360.Activities;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;



/**
 * Created by Mac on 11/07/16.
 */
@ReportsCrashes(
        httpMethod = HttpSender.Method.PUT,
        reportType = HttpSender.Type.JSON,
        formUri = "https://technologyport.cloudant.com/acra-myapp/_design/acra-storage/_update/report",
        //formUriBasicAuthLogin = "technologyport",
        //formUriBasicAuthPassword = "p@ssw0rd"
        formUriBasicAuthLogin = "ndrounclogrearyouldentac",
        formUriBasicAuthPassword = "193fc53f9303a167ea3a6c5e19ac7b7b3da58676"
        //formKey = "" // This is required for backward compatibility but not used
//        customReportContent = {
//                ReportField.APP_VERSION_CODE,
//                ReportField.APP_VERSION_NAME,
//                ReportField.ANDROID_VERSION,
//                ReportField.PACKAGE_NAME,
//                ReportField.REPORT_ID,
//                ReportField.BUILD,
//                ReportField.STACK_TRACE
//        },
//        mode = ReportingInteractionMode.TOAST,
//        resToastText = R.string.toast_crash
)
public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
        MultiDex.install(this);

    }
}