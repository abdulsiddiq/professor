package college.com.collegenetwork;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Krypto on 01-12-2017.
 */

public class MyApplicationClass extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
