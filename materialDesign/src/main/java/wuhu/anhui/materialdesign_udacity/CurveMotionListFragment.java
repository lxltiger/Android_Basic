package wuhu.anhui.materialdesign_udacity;


import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * 演示ShareElement 动画切换的不同方式
 */
public class CurveMotionListFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_curve_motion_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new MessageAdapter());
    }


    class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder>  {
        private final int[] COLORS = new int[] { 0xff956689, 0xff80678A, 0xff6A6788,
                0xff546683, 0xff3F657B, 0xff3F657B };

        @Override
        public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.message, parent, false);
            return new MessageViewHolder(view);
        }

        @TargetApi(21)
        @Override
        public void onBindViewHolder(final MessageViewHolder holder, final int position) {
           final int color = COLORS[position % COLORS.length];
            Log.d("MessageAdapter", "color:" + color);
            holder.avatar.setBackgroundTintList(ColorStateList.valueOf(color));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CurvedMotionDetailActivity.class);
                    Log.d("MessageAdapter", "holder.getAdapterPosition():" + holder.getAdapterPosition());
                    boolean curve = (position % 2 == 0);
                    intent.putExtra(CurvedMotionDetailActivity.EXTRA_COLOR, color);
                    intent.putExtra(CurvedMotionDetailActivity.EXTRA_CURVE, curve);
                    getActivity().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                            getActivity(), holder.avatar, holder.avatar.getTransitionName()).toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return 36;
        }


    }

    private static class MessageViewHolder extends RecyclerView.ViewHolder{
        View avatar;
        public MessageViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }
}
