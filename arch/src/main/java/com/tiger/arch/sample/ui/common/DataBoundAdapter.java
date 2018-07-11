package com.tiger.arch.sample.ui.common;

import android.annotation.SuppressLint;
import android.databinding.ViewDataBinding;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public abstract class DataBoundAdapter<T, V extends ViewDataBinding> extends RecyclerView.Adapter<DataBoundViewHolder<V>> {

    private List<T> items;
    private int dataVersion = 0;

    @NonNull
    @Override
    public DataBoundViewHolder<V> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        V viewDataBinding = createBinding(parent);
        return new DataBoundViewHolder<>(viewDataBinding);
    }

    @Override
    public final void onBindViewHolder(@NonNull DataBoundViewHolder<V> holder, int position) {
        bind(items.get(position), holder.binding);
        holder.binding.executePendingBindings();

    }

    @SuppressLint("StaticFieldLeak")
    public void replace(List<T> update) {
        dataVersion++;
        if (items == null) {
            if (update == null) {
                return;
            }
            items = update;
            notifyDataSetChanged();

        } else if (update == null) {
            int size = items.size();
            items = null;
            notifyItemRangeRemoved(0, size);
        } else {
            final int version = dataVersion;
            final List<T> oldItems = items;
            new AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                @Override
                protected DiffUtil.DiffResult doInBackground(Void... voids) {
                    return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                        @Override
                        public int getOldListSize() {
                            return oldItems.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return update.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            return DataBoundAdapter.this.areItemTheSame(oldItems.get(oldItemPosition), update.get(newItemPosition));
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            return DataBoundAdapter.this.areContentTheSame(oldItems.get(oldItemPosition), update.get(newItemPosition));

                        }
                    });
                }

                @Override
                protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                    if (version != dataVersion) {
                        return;
                    }
                    items = update;
                    diffResult.dispatchUpdatesTo(DataBoundAdapter.this);
                }
            }.execute();
        }
    }


    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    protected abstract boolean areItemTheSame(T oldItem, T newItem);

    protected abstract boolean areContentTheSame(T oldItem, T newItem);

    abstract V createBinding(ViewGroup parent);

    abstract void bind(T t, V binding);
}
