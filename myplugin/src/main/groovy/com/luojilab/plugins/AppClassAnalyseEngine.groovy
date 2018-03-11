package com.luojilab.plugins

import com.android.SdkConstants
import javassist.ClassPool
import javassist.CtClass
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile;

/**
 * Created by liushuo on 2018/3/4.
 */

public class AppClassAnalyseEngine {
  public static final ClassPool sClassPool = ClassPool.getDefault()

  public static void insertSdkClassSearchPath(Project project) {
    project.android.bootClasspath.each {
      sClassPool.insertClassPath((String) it.absolutePath)
      println("boot class path:" + it.absolutePath)
    }
  }

  private Map<String,  Set<String>> mSubClassMap = new HashMap<>();

  public static AppClassAnalyseEngine sAnalyseEngine = new AppClassAnalyseEngine();

  public Map<String,  Set<String>> getSubClassMap() {
    return mSubClassMap
  }

  public void analyseClassInJarFile(String jarPath) {
    sClassPool.insertClassPath(jarPath)

    JarFile jarFile = new JarFile(jarPath)

    Enumeration<JarEntry> classes = jarFile.entries();
    while (classes.hasMoreElements()) {
      JarEntry libClass = classes.nextElement();
      String classPathInJar = libClass.getName();

      println("find class in jar:" + classPathInJar)

      if (!classPathInJar.endsWith(SdkConstants.DOT_CLASS)) return

      String className = Utils.classPath2ClassName(classPathInJar);
      if (Utils.isSystemClass(className)) return

      CtClass ctClass = sClassPool.getCtClass(className)
      if (checkListViewSubClass(ctClass)) {
        println("find subclass of ListView")
      }
    }
  }


  public void analyseClassInDirectory(String path) {
    sClassPool.insertClassPath(path)

    File dir = new File(path)
    if (dir.isDirectory()) {

      println("directory path:" + path)

      dir.eachFileRecurse {File file ->

        String fullyFilePath = file.absolutePath
        if (!fullyFilePath.endsWith(SdkConstants.DOT_CLASS)) return
        println("find class file path:" + fullyFilePath)

        String fullyClassNameNoneSuffix = parseFullyClassNameNoneSuffix(path, fullyFilePath)
        if (Utils.isSystemClass(fullyClassNameNoneSuffix)) return


        CtClass ctClass = sClassPool.getCtClass(fullyClassNameNoneSuffix)

        if (checkListViewSubClass(ctClass)) {
          println("find subclass of ListView")
        }
      }
    }

  }

  private static String parseFullyClassNameNoneSuffix(String path, String fullFilePath) {
    int startIndex = fullFilePath.indexOf(path) + path.length() + 1 //过滤掉末尾 '/' 字符
    int endIndex = fullFilePath.lastIndexOf(SdkConstants.DOT_CLASS)

    String result = fullFilePath.subSequence(startIndex, endIndex)
    return result.replaceAll("/", ".")
  }

  private boolean checkListViewSubClass(CtClass ctClass) {
    CtClass listViewClass = sClassPool.get(SdkConstants.FQCN_LIST_VIEW)

    boolean isSubClass = ctClass.subclassOf(listViewClass)
    if (isSubClass) {
       Set<String> list = mSubClassMap.get(SdkConstants.FQCN_LIST_VIEW)
      if (list == null) {
        list = new ArrayList<>()
        mSubClassMap.put(SdkConstants.FQCN_LIST_VIEW, list)
      }
      list.add(ctClass.getName())
    }

    return isSubClass;
  }

}
