package org.openbot.modelManagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.koushikdutta.ion.Ion;
import java.io.File;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.openbot.databinding.ItemModelBinding;
import org.openbot.tflite.Model;
import org.openbot.utils.FileUtils;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ViewHolder> {

  private List<Model> mValues;
  private final OnItemClickListener<Model> itemClickListener;

  public interface OnItemClickListener<T> {
    void onItemClick(T item);

    void onModelDownloadClicked();

    void onModelDownloaded(boolean status, Model mItem);

    void onModelDelete(Model mItem);
  }

  public ModelAdapter(List<Model> items, OnItemClickListener<Model> itemClickListener) {
    mValues = items;
    this.itemClickListener = itemClickListener;
  }

  @NotNull
  @Override
  public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
    return new ViewHolder(
        ItemModelBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.mItem = mValues.get(position);
    holder.title.setText(FileUtils.nameWithoutExtension(mValues.get(position).getName()));
    holder.title.setOnClickListener(v -> itemClickListener.onItemClick(holder.mItem));
    holder.imgDownload.setOnClickListener(
        v -> {
          itemClickListener.onModelDownloadClicked();
          Ion.with(holder.itemView.getContext())
              .load(holder.mItem.path)
              .progress(
                  (downloaded, total) -> {
                    System.out.println("" + downloaded + " / " + total);
                    holder.progressBar.setProgress((int) (downloaded * 100 / total));
                  })
              //              .write(new File("/sdcard/openbot/tf.tflite"))
              .write(
                  new File(
                      holder.itemView.getContext().getFilesDir()
                          + File.separator
                          + holder.mItem.name))
              .setCallback(
                  (e, file) -> {
                    holder.progressBar.setProgress(0);
                    itemClickListener.onModelDownloaded(e == null, holder.mItem);
                  });
        });

    holder.imgDownload.setVisibility(
        (holder.mItem.pathType == Model.PATH_TYPE.URL) ? View.VISIBLE : View.GONE);
    holder.imgDelete.setVisibility(
        (holder.mItem.pathType == Model.PATH_TYPE.FILE) ? View.VISIBLE : View.GONE);
    holder.imgDelete.setOnClickListener(v -> itemClickListener.onModelDelete(holder.mItem));
    holder.title.setAlpha((holder.mItem.pathType == Model.PATH_TYPE.URL) ? 0.7f : 1f);
  }

  @Override
  public int getItemCount() {
    return mValues.size();
  }

  public void setItems(List<Model> modelList) {
    this.mValues = modelList;
    notifyDataSetChanged();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public final TextView title;
    public final FrameLayout imgDownload;
    public final ImageView imgDelete;
    public Model mItem;
    public ProgressBar progressBar;

    public ViewHolder(ItemModelBinding binding) {
      super(binding.getRoot());

      title = binding.title;
      imgDownload = binding.downloadModel;
      progressBar = binding.progressBar;
      imgDelete = binding.deleteModel;
    }
  }
}
