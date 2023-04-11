package org.openbot.projects;

import android.content.Context;
import android.util.SparseArray;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.api.services.drive.model.File;
import java.util.List;
import org.openbot.R;
import org.openbot.main.CommonRecyclerViewAdapter;

public class DriveProjectsAdapter extends CommonRecyclerViewAdapter<File> {

  public DriveProjectsAdapter(
      @NonNull Context context,
      @NonNull List<File> dataList,
      @NonNull SparseArray<int[]> resLayoutAndViewIds) {
    super(context, dataList, resLayoutAndViewIds);
  }

  @Override
  public int getItemResLayoutType(int position) {
    return R.layout.projects_list_view;
  }

  @Override
  public void bindDataToItem(MyViewHolder holder, File data, int position) {
    TextView tvName = (TextView) holder.mViews.get(R.id.project_name);
    String nameWithoutExtension = data.getName().substring(0, data.getName().length() - 3);
    if (nameWithoutExtension.length() > 7) {
      nameWithoutExtension = nameWithoutExtension.substring(0, 7) + "...";
    }
    tvName.setText(nameWithoutExtension);
    System.out.println("sanjeev id = " + data.getId());
  }
}
