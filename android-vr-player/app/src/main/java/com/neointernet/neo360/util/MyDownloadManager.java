package com.neointernet.neo360.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Created by HSH on 16. 5. 3..
 */
public class MyDownloadManager {


    private final Context context;
    private DownloadManager mDownloadManager; //다운로드 매니저.
    private Long mDownloadQueueId; //다운로드 큐 아이디..
    private String mFileName; //파일다운로드 완료후...파일을 열기 위해 저장된 위치를 입력해둔다.

    private String storagePath;
    private String TAG = "filepathLog";

    public MyDownloadManager(Context context) {
        this.context = context;
        storagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/temp/";
    }

    public boolean downloadFile(String url) {
        List<String> pathSegmentList = Uri.parse(url).getPathSegments();
        mFileName = pathSegmentList.get(pathSegmentList.size() - 1);
        if (!checkExistFile(mFileName)) {
            if (mDownloadManager == null)
                mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setTitle("==타이틀==");
            request.setDescription("==설명==");
            new File(storagePath).mkdirs();
//            Environment.getExternalStoragePublicDirectory("/360Videos").mkdirs();  //경로는 입맛에 따라...바꾸시면됩니다.
//            request.setDestinationInExternalPublicDir("/360Videos/", mFileName);
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/temp").mkdirs(); //경로는 입맛에 따라...바꾸시면됩니다.
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/temp/", pathSegmentList.get(pathSegmentList.size() - 1));

            mDownloadQueueId = mDownloadManager.enqueue(request);
            return true;
        }
        return false;
    }

    public BroadcastReceiver createReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    Toast.makeText(context, "다운로드가 완료되었습니다.", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    public boolean checkExistFile(String fileName) {
        String filePath = storagePath + fileName;
        File file = new File(filePath);
        boolean check = file.exists();
        Log.i(TAG, "filePath : " + filePath + " Check : " + String.valueOf(check));
        check = file.isFile();
        Log.i(TAG, "filePath : " + filePath + " isFileCheck : " + String.valueOf(check));
        return check;
    }

    public String getStoragePath() {
        return storagePath;
    }
}
