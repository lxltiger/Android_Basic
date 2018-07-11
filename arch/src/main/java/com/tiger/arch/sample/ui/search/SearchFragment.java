package com.tiger.arch.sample.ui.search;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.tiger.arch.R;
import com.tiger.arch.databinding.FragmentSearchBinding;
import com.tiger.arch.sample.ui.common.RepoListAdapter;
import com.tiger.arch.sample.utils.AutoClearValue;
import com.tiger.arch.sample.vo.Repo;
import com.tiger.arch.sample.vo.Resource;

import java.util.List;

public class SearchFragment extends Fragment {
    private final static String TAG = SearchFragment.class.getSimpleName();
    private SearchViewModel mSearchViewModel;
    private AutoClearValue<FragmentSearchBinding> mBinding;
    private AutoClearValue<RepoListAdapter> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentSearchBinding searchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        mBinding=new AutoClearValue<>(this,searchBinding);
        return searchBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSearchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        initRecycleView();
        initSearchInputListener();
        RepoListAdapter repoListAdapter=new RepoListAdapter(true, new RepoListAdapter.RepoClickCallback() {
            @Override
            public void onClick(Repo repo) {
                Log.d(TAG, repo.toString());
            }
        });
        mBinding.get().repoList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.get().repoList.setAdapter(repoListAdapter);
        adapter = new AutoClearValue<>(this,repoListAdapter);


    }

    private void initRecycleView() {
        mBinding.get().repoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,int dx,int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (adapter.get().getItemCount()-1==layoutManager.findLastVisibleItemPosition()) {
                    Log.d(TAG, "end reached");
                }

            }
        });
//        监听repo变化
        mSearchViewModel.getResults().observe(this, resource -> {
            Log.d(TAG, "onChanged: "+resource);
            mBinding.get().setSearchResource(resource);
            mBinding.get().setResultCount((resource==null||resource.data==null)?0:resource.data.size());
            adapter.get().replace(resource == null ? null : resource.data);
            mBinding.get().executePendingBindings();
        });



    }

    private void initSearchInputListener() {
        mBinding.get().searchText.setOnEditorActionListener((v, actionId, event) -> {
            if (EditorInfo.IME_ACTION_SEARCH==actionId) {
                search();
                return true;
            }
            return false;

        });

        mBinding.get().searchText.setOnKeyListener((v, keyCode, event) -> {
            if(keyCode==KeyEvent.KEYCODE_ENTER&&event.getAction()==KeyEvent.ACTION_DOWN){
                search();
                return true;
            }
            return false;

        });
    }

//    begin to search
    private void search() {
        String query = mBinding.get().searchText.getText().toString();
        Log.d(TAG, "search: "+query);
        mBinding.get().setQuery(query);
        // TODO: 2018/6/26 0026 close ime
        mSearchViewModel.startQuery(query);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}