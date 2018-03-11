package com.luojilab.plugins;

import com.android.SdkConstants;

/**
 * Created by liushuo on 2018/3/10.
 */

public class Utils {
  public static final String sPackagePrefix = "android.";

  public static boolean isSystemClass(String fullyClassName) {
    return fullyClassName.startsWith(sPackagePrefix);
  }

  public static String classPath2ClassName(String classPathInJar) {
    int startIndex = 0;
    int endIndex = classPathInJar.lastIndexOf(SdkConstants.DOT_CLASS)
    classPathInJar = classPathInJar.substring(startIndex, endIndex);

    return classPathInJar.replaceAll('/', '.');
  }
}
