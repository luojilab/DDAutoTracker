package com.luojilab.plugins

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import javassist.CtClass
import javassist.CtMethod
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

public class AutoTrackerTransform extends Transform {

  private Project mProject

  AutoTrackerTransform(Project project) {
    mProject = project
  }

  @Override
  String getName() {
    return "AutoTrackerTransform"
  }

  @Override
  Set<QualifiedContent.ContentType> getInputTypes() {
    return TransformManager.CONTENT_CLASS
  }

  @Override
  Set<? super QualifiedContent.Scope> getScopes() {
    return TransformManager.SCOPE_FULL_PROJECT
  }

  @Override
  boolean isIncremental() {
    return false
  }

  @Override
  void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
    TransformOutputProvider outputProvider = transformInvocation.outputProvider

    AppClassAnalyseEngine.insertSdkClassSearchPath(mProject)

    transformInvocation.inputs.each {TransformInput input ->

      input.jarInputs.each {JarInput jarInput ->
        //分析jar中的文件
        AppClassAnalyseEngine.sAnalyseEngine.analyseClassInJarFile(jarInput.file.absolutePath)

        copyJarInput2Dest(jarInput, outputProvider)

      }

      input.directoryInputs.each {DirectoryInput directoryInput ->
        //分析目录中的文件
        AppClassAnalyseEngine.sAnalyseEngine.analyseClassInDirectory(directoryInput.file.absolutePath)

        copyDirectoryInput2Dest(directoryInput, outputProvider)
      }

    }

    //收集完毕程序中所有自定义子类对应关系,插入代码
    String dataStrategyConfigurationClassName = "com.luojilab.strategy.DataStrategyConfiguration"
    CtClass dataStrategyConfigurationClass = AppClassAnalyseEngine.sClassPool.get(dataStrategyConfigurationClassName)
    Map<String,  Set<String>> subClassMap = AppClassAnalyseEngine.sAnalyseEngine.getSubClassMap();
    Set<Map.Entry<String,  Set<String>>> sets = subClassMap.entrySet();
    Iterator<Map.Entry<String,  Set<String>>> itr = sets.iterator();
    while (itr.hasNext()) {
      Map.Entry<String,  Set<String>> entry = itr.next()
      String key = entry.getKey()
      String value = entry.getValue()

      println("遍历分析结果：" + value)

//      CtMethod ctMethod = dataStrategyConfigurationClass.getDeclaredMethod("configureDataStrategies")
//      ctMethod.insertAfter("")
    }

  }

  /**
   * 拷贝目录到指定的输出位置
   * @param directoryInput
   * @param outputProvider
   */
  private
  static void copyDirectoryInput2Dest(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
    File dest = outputProvider.getContentLocation(directoryInput.name
        , directoryInput.contentTypes
        , directoryInput.scopes
        , Format.DIRECTORY)

    FileUtils.copyDirectory(directoryInput.file, dest)

    println("源文件：" + directoryInput.file)
    println("目标文件：" + dest)
  }

  /**
   * 拷贝jar到指定的输出位置
   * @param jarInput
   * @param outputProvider
   */
  private static void copyJarInput2Dest(JarInput jarInput, TransformOutputProvider outputProvider) {
    String jarName = jarInput.name
    String md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)

    if (jarName.endsWith(".jar")) {
      jarName = jarName.substring(0, jarName.length() - 4)
    }

    File dest = outputProvider.getContentLocation(jarName + md5Name
        , jarInput.contentTypes
        , jarInput.scopes
        , Format.JAR)

    if (dest.exists()) {
      dest.delete()
    }

    FileUtils.copyFile(jarInput.file, dest)

    println("源jar文件：" + jarInput.file)
    println("目标jar文件：" + dest)
  }
}