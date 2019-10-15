package com.example.engineerdemo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.engineerdemo.model.DataModel;
import com.example.engineerdemo.webcalls.MyApiService;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DataViewModel extends ViewModel {
    private int pagenumber = 1;
    private Disposable mDisposable = null;

    private MutableLiveData<DataModel> dataModelMutableLiveData;

    public DataViewModel() {
        mDisposable = new Disposable() {
            @Override
            public void dispose() {

            }

            @Override
            public boolean isDisposed() {
                return false;
            }
        };
        dataModelMutableLiveData = new MutableLiveData<>();
        loadTagsData();
    }

    private void loadTagsData() {
        dataModelMutableLiveData.setValue(new DataModel());
    }

    public void clearViewDataModel() {
        dataModelMutableLiveData.setValue(null);
    }

    public MutableLiveData<DataModel> getDataModelMutableLiveData() {
        return dataModelMutableLiveData;
    }

    public void getTagsData() {
        mDisposable = MyApiService.getService().getTagsData(pagenumber).subscribe(new Consumer<DataModel>() {
            @Override
            public void accept(DataModel dataModel) throws Exception {
                if (dataModel.getHits() != null) {
                    dataModelMutableLiveData.setValue(dataModel);
                    mDisposable.dispose();
                } else {
                    mDisposable.dispose();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });
    }
}
