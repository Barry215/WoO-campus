package com.example.barry215.woo.DB;

import android.content.Context;

import com.example.barry215.greendao.DaoMaster;
import com.example.barry215.greendao.DaoSession;


/**
 * Created by barry215 on 2016/4/24.
 */
public class DaoHelper {
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,"notes-db", null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

}
