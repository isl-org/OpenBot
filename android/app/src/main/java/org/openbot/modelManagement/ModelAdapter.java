package org.openbot.modelManagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.openbot.databinding.ItemModelBinding;
import org.openbot.tflite.Model;

import java.util.List;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ViewHolder> {

  private List<Model> mValues;
  private final OnItemClickListener<Model> itemClickListener;

  public interface OnItemClickListener<T> {
    void onItemClick(T item);

    void onDownloadClick(T item);
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
    holder.title.setText(mValues.get(position).getName());
    holder.title.setOnClickListener(v -> itemClickListener.onItemClick(holder.mItem));
    holder.imgDownload.setOnClickListener(v -> itemClickListener.onDownloadClick(holder.mItem));

    holder.imgDownload.setVisibility(
        (holder.mItem.pathType == Model.PATH_TYPE.URL) ? View.VISIBLE : View.GONE);
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
    public final ImageView imgDownload;
    public Model mItem;

    public ViewHolder(ItemModelBinding binding) {
      super(binding.getRoot());

      title = binding.title;
      imgDownload = binding.downloadModel;
    }
  }
}
