package wuhu.anhui.materialdesign_udacity;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * 可拉升的toolbar
 * 使用新字体
 */
public class DynamicSurfaceFragment extends Fragment {

    private Typeface mTypeface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //  加载字体
        mTypeface = Typeface.createFromAsset(context.getAssets(), "Courgette-Regular.ttf");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dynastic_surface, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar_layout);
        toolbarLayout.setTitle(getString(R.string.app_name));
        TextView textView = (TextView) view.findViewById(R.id.textView);
        //使用资源目录的字体
        textView.setTypeface(mTypeface);

    }
}
