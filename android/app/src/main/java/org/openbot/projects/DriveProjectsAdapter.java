package org.openbot.projects;

import android.content.Context;
import android.util.SparseArray;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    TextView projectName = (TextView) holder.mViews.get(R.id.project_name);
    TextView updatedDate = (TextView) holder.mViews.get(R.id.project_date);
    String projectNameModified = data.getName().substring(0, data.getName().length() - 3);
    if (projectNameModified.length() > 10) {
      projectNameModified = projectNameModified.substring(0, 10) + "...";
    }
    projectName.setText(projectNameModified);
    DateTime dateTime =
        new com.google.api.client.util.DateTime(String.valueOf(data.getModifiedTime()));
    Instant instant = Instant.ofEpochMilli(dateTime.getValue());
    LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy");
    String formattedDate = outputFormatter.format(localDate);
    updatedDate.setText(formattedDate);
  }
}
