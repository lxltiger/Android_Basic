package wuhu.anhui.materialdesign_udacity;


import android.animation.Animator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * a demo of CoordinatorLayout with
 * Toolbar recyclerView and Floating button
 */
public class CollaspingFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collasping, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar_layout);
        toolbarLayout.setTitle(getString(R.string.app_name));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new NormalAdapter());
        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "hei", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class NormalAdapter extends RecyclerView.Adapter<NormalViewHolder>  {



        @Override
        public NormalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).
                    inflate(android.R.layout.simple_list_item_1, parent,false);

            return new NormalViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NormalViewHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemCount() {
            return 20;
        }


    }


    private  static class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextView;
       static int green;
        static int white;
        public NormalViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(android.R.id.text1);
            itemView.setOnClickListener(this);
            if (green == 0) {

                green = itemView.getContext().getResources().getColor(android.R.color.holo_green_dark);
            }
            if (white == 0) {

                white = itemView.getContext().getResources().getColor(R.color.background_material_light);
            }

        }

        public void bind() {
            int position=getAdapterPosition();
            mTextView.setText(String.format("game from china %s",position));
        }

        @Override
        public void onClick(View v) {
            ColorDrawable colorDrawable = (ColorDrawable) v.getBackground();
            // 是否是绿色背景
            boolean isViggle=colorDrawable!=null&&colorDrawable.getColor()==green;
            //获取?
            int finalRadius= (int) Math.hypot(v.getWidth(), v.getHeight());
            if (isViggle) {
                v.setBackgroundColor(white);
                mTextView.setText(String.format("game from china %s",getAdapterPosition()));
            }else{
                Animator animation = ViewAnimationUtils.createCircularReveal(v, v.getWidth()/2, v.getHeight()/2, 0, finalRadius);
                mTextView.setText("duang");
                v.setBackgroundColor(green);
                animation.start();

            }
        }
    }

}
