package com.tiger.arch.sample.ui.common;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tiger.arch.R;
import com.tiger.arch.databinding.ItemRepoBinding;
import com.tiger.arch.sample.utils.Objects;
import com.tiger.arch.sample.vo.Repo;

public class RepoListAdapter extends DataBoundAdapter<Repo,ItemRepoBinding> {

    private final boolean showFullName;
    private final RepoClickCallback mCallback;

    public RepoListAdapter(boolean showFullName, RepoClickCallback callback) {
        this.showFullName = showFullName;
        mCallback = callback;
    }

    @Override
    protected boolean areItemTheSame(Repo oldItem, Repo newItem) {
        return Objects.equals(oldItem.getOwner(), newItem.getOwner()) &&
                Objects.equals(oldItem.getName(), newItem.getName());
    }

    @Override
    protected boolean areContentTheSame(Repo oldItem, Repo newItem) {
        return Objects.equals(oldItem.getDescription(), newItem.getDescription()) &&
                oldItem.getStars() == newItem.getStars();
    }

    @Override
    ItemRepoBinding createBinding(ViewGroup parent) {
        LayoutInflater from = LayoutInflater.from(parent.getContext());
        ItemRepoBinding binding = DataBindingUtil.inflate(from, R.layout.item_repo, parent, false);
        binding.setShowFullName(showFullName);
        binding.getRoot().setOnClickListener(v -> {
            Repo repo = binding.getRepo();
            if (null!=repo&&mCallback != null) {
                mCallback.onClick(repo);
            }
        });
        return binding;
    }


    @Override
    void bind(Repo repo, ItemRepoBinding binding) {
        binding.setRepo(repo);

    }


    public interface RepoClickCallback{
        void onClick(Repo repo);
    }


}
