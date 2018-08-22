package myapp.training.newitventure.com.myinstalledappsapplication;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

class GetAllAppsTask extends AsyncTask<Void, Void, List<ApplicationInfo>>{
    private MainActivity mainActivity;
    private List<ApplicationInfo> apps;
    private PackageManager packageManager;

    GetAllAppsTask(MainActivity mainActivity, List<ApplicationInfo> apps, PackageManager pm) {
        this.mainActivity = mainActivity;
        this.apps = apps;
        this.packageManager = pm;
    }

    protected List<ApplicationInfo> doInBackground(Void... params) {
        apps = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
        return apps;
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {

        ArrayList<ApplicationInfo> applist = new ArrayList<>();
        for (ApplicationInfo applicationInfo : list) {
            try {
                if (packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null) {
                    applist.add(applicationInfo);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return applist;
    }

    protected void onPostExecute(List<ApplicationInfo> list) {
        super.onPostExecute(list);
        mainActivity.callBackDataFromAsynctask(list);
    }
}