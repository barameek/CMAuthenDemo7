package com.codemobiles.util;

/*
 * Copyright (C) 2009 codemobiles.com.
 * http://www.codemobiles.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * author: Chaiyasit Tayabovorn 
 * email: chaiyasit.t@gmail.com
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {

  private SQLiteDatabase sqliteDB;

  public DBAdapter(Context context) {
    final String dbFilePath = CMAssetBundle.getAppPackagePath(context) + "/Account.db";
    sqliteDB = SQLiteDatabase.openDatabase(dbFilePath, null, SQLiteDatabase.OPEN_READWRITE);

  }

  public void update(final UserBean bean) {
    String selection = UserBean.COLUMN_USERNAME + "=?" ;
    ContentValues values = new ContentValues();
    values.put(UserBean.COLUMN_USERNAME, bean.username);
    values.put(UserBean.COLUMN_PASSWORD, bean.password);
    values.put(UserBean.COLUMN_PASSWORD_REM, bean.isPasswordRemembered);
    sqliteDB.update(UserBean.TABLE_NAME, values, selection, new String[] { bean.username });
  }


  public void insert(final UserBean bean) {
    ContentValues values = new ContentValues();
    values.put(UserBean.COLUMN_USERNAME, bean.username);
    values.put(UserBean.COLUMN_PASSWORD, bean.password);
    values.put(UserBean.COLUMN_PASSWORD_REM, bean.isPasswordRemembered);
    sqliteDB.insert(UserBean.TABLE_NAME, "", values);
  }


  public final UserBean query(final String username) {
    // An array specifying which columns to return.
    String columns[] = new String[] { UserBean.COLUMN_USERNAME, UserBean.COLUMN_PASSWORD, UserBean.COLUMN_PASSWORD_REM };
    String selection = UserBean.COLUMN_USERNAME + "=?";
    UserBean userBn = null;

    Cursor cur = null;

    try {
      cur = sqliteDB.query(UserBean.TABLE_NAME, columns, selection, new String[] { username }, null, null, null);

      // check if found any query result
      if (cur.getCount() > 0) {
        cur.moveToFirst();
        userBn = new UserBean();
        userBn.username = cur.getString(cur.getColumnIndex(UserBean.COLUMN_USERNAME));
        userBn.password = cur.getString(cur.getColumnIndex(UserBean.COLUMN_PASSWORD));
        userBn.isPasswordRemembered = Integer.valueOf(cur.getInt(cur.getColumnIndex(UserBean.COLUMN_PASSWORD_REM)));
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    } finally {
      cur.close();
    }

    return userBn;
  }
}
