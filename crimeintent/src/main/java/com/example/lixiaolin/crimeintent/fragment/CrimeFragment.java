package com.example.lixiaolin.crimeintent.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.lixiaolin.crimeintent.R;
import com.example.lixiaolin.crimeintent.entity.Crime;
import com.example.lixiaolin.crimeintent.entity.CrimeLab;
import com.example.lixiaolin.crimeintent.utils.PictureUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.CheckedInputStream;

/**
 * A simple {@link Fragment} subclass.
 * if taking photo ,it requires permission below 19
 */
public class CrimeFragment extends Fragment implements TextWatcher, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private static final String CRIME_POSITION = "com.lxl.crime.position";
    private static final int CRIME_DATE = 0;
    private static final int CRIME_CONTACT = 1;
    private static final int CRIME_TAKE_PICTURE = 2;
    private Crime mCrime;
    private EditText mEditTextTitle;
    private Button mButtonDate;
    private CheckBox mCheckBoxSolved;
    private Button mButtonChooseContact;
    private Button mButtonSendCrime;
    private ImageView mImageViewPhoto;
    private ImageButton mImageButtonTakePhoto;
    private SimpleDateFormat mSimpleDateFormat;

    private Intent mPickContact;
    //file pointing to the picture taken from camera;
    private File mFilePhoto;
    private PackageManager mPackageManager;
    private Intent mIntent_take_picture;

    //use this way to create instance is flexible for any activity who
    //want to hold this fragment
    public static CrimeFragment getInstance(int position) {
        CrimeFragment fragment = new CrimeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CRIME_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int position = getArguments().getInt(CRIME_POSITION);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime().get(position);
        mFilePhoto = CrimeLab.getInstance(getActivity()).getPhotoFile(mCrime);
        if (mFilePhoto != null) {
            Log.d("pictureUtils", "mFilePhoto" + mFilePhoto.getPath());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crime, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPackageManager = getActivity().getPackageManager();
        initPhoto(view);
        mEditTextTitle = (EditText) view.findViewById(R.id.et_crime_title);
        mEditTextTitle.setText(mCrime.getTitle());
        mEditTextTitle.addTextChangedListener(this);
        mButtonDate = (Button) view.findViewById(R.id.btn_date);
        formatDate(mCrime.getDate());
        mButtonDate.setOnClickListener(this);
        mCheckBoxSolved = (CheckBox) view.findViewById(R.id.cb_isSolved);
        mCheckBoxSolved.setChecked(mCrime.isSolved());
        mCheckBoxSolved.setOnCheckedChangeListener(this);
        //choose suspect
        mButtonChooseContact = (Button) view.findViewById(R.id.btn_choose_contact);
        //the intent to pick contact from system
        mPickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //check activity to response to this intent
        if (mPackageManager.resolveActivity(mPickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mButtonChooseContact.setEnabled(false);
        } else {
            mButtonChooseContact.setOnClickListener(this);
        }
        if (mCrime.getSuspect() != null) {
            mButtonChooseContact.setText(mCrime.getSuspect());
        }
        mButtonSendCrime = (Button) view.findViewById(R.id.btn_send_crime);
        mButtonSendCrime.setOnClickListener(this);

    }

    private void initPhoto(View view) {
        mImageViewPhoto = (ImageView) view.findViewById(R.id.iv_photo);
        mImageButtonTakePhoto = (ImageButton) view.findViewById(R.id.ib_take_photo);
        mIntent_take_picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //return true when external storage is available and proper app exists to take photo
        boolean enable = mFilePhoto != null && mIntent_take_picture.resolveActivity(mPackageManager) != null;
        if (enable) {
            Uri uri = Uri.fromFile(mFilePhoto);
            mIntent_take_picture.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            mImageButtonTakePhoto.setOnClickListener(this);
        } else {
            mImageButtonTakePhoto.setEnabled(enable);
        }
        updatePhoto();
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mCrime.setTitle(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCrime.setIsSolved(isChecked);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //user implicit intent to get contact Info
            case R.id.btn_choose_contact:
                startActivityForResult(mPickContact, CRIME_CONTACT);
                break;
            case R.id.btn_send_crime:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "heihe");
                intent.putExtra(Intent.EXTRA_SUBJECT, "email");
                intent = Intent.createChooser(intent, "title");
                startActivity(intent);
                break;
            case R.id.btn_date:
                DatePickerFragment fragment = DatePickerFragment.newInstance(mCrime.getDate());
                fragment.show(getFragmentManager(), "DatePicker");
                fragment.setTargetFragment(CrimeFragment.this, CRIME_DATE);
                break;
            case R.id.ib_take_photo:
                startActivityForResult(mIntent_take_picture, CRIME_TAKE_PICTURE);
                break;
        }


    }

    private void formatDate(Date date) {
        if (mSimpleDateFormat == null) {
            mSimpleDateFormat = new SimpleDateFormat("E yyyy-MM-dd", Locale.CHINA);
        }
        String format_date = mSimpleDateFormat.format(date);
        mButtonDate.setText(format_date);
    }

    private void updatePhoto() {
        if (mFilePhoto != null && mFilePhoto.exists()) {
//            Bitmap bitmap = PictureUtils.getScaleBitmap(getActivity(), mFilePhoto.getPath());
            Bitmap bitmap = PictureUtils.getScaleBitmap(mFilePhoto.getPath(),160,160);
            Log.d("pictureUtils", "bitmap width" + bitmap.getWidth());
            Log.d("pictureUtils", "bitmap Height" + bitmap.getHeight());
            mImageViewPhoto.setImageBitmap(bitmap);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case CRIME_DATE:
                    Date date = (Date) data.getSerializableExtra(DatePickerFragment.DATE);
                    mCrime.setDate(date);
                    formatDate(mCrime.getDate());
                    break;
                case CRIME_CONTACT:
                    Uri uri = data.getData();
                    String[] queryField = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
                    Cursor cursor = getActivity().getContentResolver().query(uri, queryField, null, null, null);
                    try {
                        if (cursor.getCount() != 0) {
                            cursor.moveToFirst();
                            String contactName = cursor.getString(0);
                            mCrime.setSuspect(contactName);
                            mButtonChooseContact.setText(contactName);
                        }
                    } finally {
                        cursor.close();
                    }
                    break;

            }
        }else if (resultCode == Activity.RESULT_OK && requestCode == CRIME_TAKE_PICTURE) {
            updatePhoto();
        }


    }
}
