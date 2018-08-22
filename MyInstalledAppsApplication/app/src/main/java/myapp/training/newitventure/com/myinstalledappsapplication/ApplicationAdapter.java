package myapp.training.newitventure.com.myinstalledappsapplication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    List<ApplicationInfo> appsList;
    List<ApplicationInfo> originalList;
    PackageManager packageManager;
    MainActivity mainActivity;
    private AppsFilter filter;

    ApplicationAdapter(MainActivity mainActivity, ArrayList<ApplicationInfo> appsList) {

        this.mainActivity = mainActivity;
        this.appsList = appsList;
        this.originalList = appsList;
        packageManager = mainActivity.getPackageManager();
    }

    @NonNull
    @Override
    public ApplicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_gridview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ApplicationAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.appName.setText(getItem(position).loadLabel(packageManager)); //get app name
        viewHolder.icon.setImageDrawable(getItem(position).loadIcon(packageManager)); //get app icon


        viewHolder.linearLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {

                    if(position == 0){
                        view.requestFocus();
                    }

                    Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_in_tv);
                    view.startAnimation(animation);
                    animation.setFillAfter(true);
                    view.setBackgroundColor(Color.parseColor("#BBBBBB"));

                }else {
                    Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_out_tv);
                    view.startAnimation(animation);
                    animation.setFillAfter(true);
                    view.setBackgroundColor(Color.parseColor("#00000000"));
                }
            }
        });

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ApplicationInfo app = appsList.get(position);

                try {
                    Intent intent = packageManager.getLaunchIntentForPackage(app.packageName);
                    mainActivity.startActivity(intent);
                    if (null != intent) {
                        mainActivity.startActivity(intent);
                    }
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }

    private ApplicationInfo getItem(int position) {
        return appsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return appsList.indexOf(getItem(position));
    }

    public Filter getFilter() {
        if (filter == null) {
            filter = new AppsFilter();
        }
        return filter;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView appName;
        LinearLayout linearLayout;

        ViewHolder(@NonNull final View root) {
            super(root);
            icon = (ImageView) root.findViewById(R.id.icon);
            appName = (TextView) root.findViewById(R.id.name);
            linearLayout = (LinearLayout) root.findViewById(R.id.linear_layout);

        }
    }

    private class AppsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<ApplicationInfo> filterList = new ArrayList<ApplicationInfo>();
                for (int i = 0; i < originalList.size(); i++) {
                    if ((originalList.get(i).loadLabel(packageManager).toString().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {

                        ApplicationInfo applicationInfo = originalList.get(i);
                        filterList.add(applicationInfo);
                    }
                }

                results.count = filterList.size();
                results.values = filterList;

            }else {
                results.count = originalList.size();
                results.values = originalList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            appsList = (ArrayList<ApplicationInfo>) results.values;
            notifyDataSetChanged();
        }

    }
}
