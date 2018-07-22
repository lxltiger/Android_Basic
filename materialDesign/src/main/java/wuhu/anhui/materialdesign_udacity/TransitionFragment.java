package wuhu.anhui.materialdesign_udacity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * 目前fragment中没实现
 * android.R.id.content ?
 */
public class TransitionFragment extends Fragment implements View.OnClickListener {

    private ImageView imageView;
    private ViewGroup mViewGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transition, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        mViewGroup = (ViewGroup) view.findViewById(android.R.id.content);
        view.findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:

                break;
        }
    }

}
