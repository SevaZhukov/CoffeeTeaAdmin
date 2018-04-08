package com.mrswimmer.coffeeteaadmin.presentation.main.fragment.new_shop;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.google.firebase.storage.UploadTask;
import com.mrswimmer.coffeeteaadmin.App;
import com.mrswimmer.coffeeteaadmin.data.model.Product;
import com.mrswimmer.coffeeteaadmin.data.model.Shop;
import com.mrswimmer.coffeeteaadmin.data.settings.Screens;
import com.mrswimmer.coffeeteaadmin.data.settings.Settings;
import com.mrswimmer.coffeeteaadmin.di.qualifier.Local;
import com.mrswimmer.coffeeteaadmin.domain.service.FilterService;
import com.mrswimmer.coffeeteaadmin.domain.service.FireService;

import java.util.ArrayList;
import java.util.Set;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class NewShopFragmentPresenter extends MvpPresenter<NewShopFragmentView> {

    @Inject
    @Local
    Router router;

    @Inject
    FireService fireService;

    @Inject
    FilterService filterService;

    @Inject
    SharedPreferences settings;

    public NewShopFragmentPresenter() {
        App.getComponent().inject(this);
    }

    public void setKindsSpinner(Spinner spinner, SpinnerAdapter adapter) {
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void createShop(String sAdress, int sBeginWork, int sEndWork, int selectedItemPosition, Uri selectedImage) {
        fireService.uploadShopImage(sAdress, selectedImage, new FireService.UploadImageCallBack() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                ArrayList<String> images = new ArrayList<>();
                images.add(String.valueOf(downloadUrl));
                Log.i("code", "url " + downloadUrl);
                Shop shop = new Shop(sAdress, sBeginWork, sEndWork, images, Settings.cities[selectedItemPosition]);
                fireService.createShop(shop);
                getViewState().showToast("Магазин создан");
                router.navigateTo(Screens.NEW_PROD);
            }

            @Override
            public void onError(Throwable e) {
                getViewState().showToast(e.getMessage());
            }
        });
    }
}