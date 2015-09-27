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
public class UserBean {
  public String username;
  public String password;
  public Integer isPasswordRemembered;

  public static String TABLE_NAME = "USERPASSWORD";
  public static String COLUMN_USERNAME = "USERNAME";
  public static String COLUMN_PASSWORD = "PASSWORD";
  public static String COLUMN_PASSWORD_REM = "PASSWORD_REM";

  public UserBean() {

  }

  public UserBean(String _username, String _password, Integer _isRemember) {
    username = _username;
    password = _password;
    isPasswordRemembered = _isRemember;
  }

  public String toString() {
    return COLUMN_USERNAME + ": " + username + "\n" + COLUMN_PASSWORD + ": " + password + "\n" + COLUMN_PASSWORD_REM + ": " + isPasswordRemembered;
  }

}
