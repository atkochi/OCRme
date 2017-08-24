package com.ashomok.imagetotext.ocr_result.tab_fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashomok.imagetotext.R;
import com.ashomok.imagetotext.language_choser.LanguageActivity;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobidevelop.spl.widget.SplitPaneLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.ashomok.imagetotext.language_choser.LanguageActivity.CHECKED_LANGUAGES;
import static com.ashomok.imagetotext.ocr_result.OCRResultActivity.LANGUAGE_ACTIVITY_REQUEST_CODE;
import static com.ashomok.imagetotext.utils.LogUtil.DEV_TAG;

/**
 * Created by iuliia on 5/31/17.
 */

//todo Forbidd splitter to go out of screen bounds. https://github.com/bieliaievays/OCRme/issues/2
public class TextFragment extends TabFragment implements View.OnClickListener {

    private long requestID = 123456789;
    private String imageLink = "gs://imagetotext-149919.appspot.com/IMG_9229.JPG";
    private String textResult = "dummy text dummy text dummy text dummy text dummy text dummy text " +
            "dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy textdummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy textdummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy textdummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy textdummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy textdummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy textdummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy textdummy text dummy text dummy text dummy text dummy text dummy text dummy text dummy text";
    private static final String TAG = DEV_TAG + TextFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.text_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void doStaff() {
        initImage();
        initText();
        initBottomPanel();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.copy_btn:
                copyTextToClipboard(textResult);
                break;
            case R.id.translate_btn:
                onTranslateClicked();
                break;
            case R.id.share_btn:
                onShareClicked();
                break;
            case R.id.bad_result_btn:
                onBadResultClicked();
                break;
            default:
                break;
        }
    }

    private void initBottomPanel() {
        View copyBtn = getActivity().findViewById(R.id.copy_btn);
        copyBtn.setOnClickListener(this);

        View translateBtn = getActivity().findViewById(R.id.translate_btn);
        translateBtn.setOnClickListener(this);

        View shareBtn = getActivity().findViewById(R.id.share_btn);
        shareBtn.setOnClickListener(this);

        View badResult = getActivity().findViewById(R.id.bad_result_btn);
        badResult.setOnClickListener(this);
    }


    private void initText() {
        EditText mTextView = (EditText) getActivity().findViewById(R.id.text);
        mTextView.setText(textResult);
    }

    private void initImage() {

        final ImageView mImageView = (ImageView) getActivity().findViewById(R.id.image);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference mStorageRef = storage.getReferenceFromUrl(imageLink);

        // Load the image using Glide
        Glide.with(getActivity())
                .using(new FirebaseImageLoader())
                .load(mStorageRef)
                .fitCenter()
                .into(mImageView);

        //scroll to centre
        final ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.image_scroll_view);
        scrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int centreHeight = mImageView.getHeight() / 2;
                int centreWidth = mImageView.getWidth() / 2;
                scrollView.scrollTo(centreWidth, centreHeight);
            }
        });
    }


    private void onBadResultClicked() {
        Intent intent = new Intent(getActivity(), LanguageActivity.class);
        intent.putExtra(CHECKED_LANGUAGES, getCurrentLanguages());
        startActivityForResult(intent, LANGUAGE_ACTIVITY_REQUEST_CODE);
    }

    @NonNull
    private ArrayList<String> getCurrentLanguages() {
        Set<String> checkedLanguageNames = obtainSavedLanguages();
        ArrayList<String> checkedLanguages = new ArrayList<>();
        checkedLanguages.addAll(checkedLanguageNames);
        return checkedLanguages;
    }

    private Set<String> obtainSavedLanguages() {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> auto = new HashSet<String>() {{
            add(getString(R.string.auto));
        }};
        TreeSet<String> checkedLanguagesNames = new TreeSet<>(sharedPref.getStringSet(CHECKED_LANGUAGES, auto));
        return checkedLanguagesNames;
    }

    private void onShareClicked() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textResult);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent,
                getActivity().getResources().getText(R.string.send_to)));
    }

    private void onTranslateClicked() {
        //todo start translate activity and chose translate language
    }

    private void copyTextToClipboard(CharSequence text) {
        ClipboardManager clipboard =
                (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getString(R.string.text_result), text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), getActivity().getString(R.string.copied),
                Toast.LENGTH_SHORT).show();
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 300 milliseconds
        v.vibrate(300);
    }
}