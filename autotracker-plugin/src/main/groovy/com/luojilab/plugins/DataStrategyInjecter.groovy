package com.luojilab.plugins;

import javassist.CtClass
import javassist.CtMethod;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by liushuo on 2018/3/11.
 */

public class DataStrategyInjecter {

  private static final
  def mDataStrategyConfigureMethods = ['android.widget.ListView': 'configureListViewDataStrategy']

  public static void inject() {
    String dataStrategyConfigurationClassName = "com.luojilab.strategy.DataStrategyConfiguration"
    CtClass dataStrategyConfigurationClass = AppClassAnalyseEngine.sClassPool.get(dataStrategyConfigurationClassName)

    Map<String, Set<String>> subClassMap = AppClassAnalyseEngine.sAnalyseEngine.getSubClassMap();
    Set<Map.Entry<String, Set<String>>> sets = subClassMap.entrySet();
    Iterator<Map.Entry<String, Set<String>>> itr = sets.iterator();
    while (itr.hasNext()) {
      Map.Entry<String, Set<String>> entry = itr.next()

      String key = entry.getKey()
      if (!mDataStrategyConfigureMethods.containsKey(key)) {
        println("没有找到控件" + key + "子类的配置方法")
        continue
      }

      Set<String> value = entry.getValue()
      if (value == null || value.isEmpty()) continue

      println("遍历分析结果：" + value)

      String methodName2Inject = mDataStrategyConfigureMethods.get(key)
      CtMethod ctMethod = dataStrategyConfigurationClass.getDeclaredMethod("configureDataStrategies")
      if (ctMethod == null) {
        println("没有找到需要注入的方法configureDataStrategies")
        continue
      }

      injectMethodCall(methodName2Inject, value, ctMethod)

      //写入注入的代码到文件
      dataStrategyConfigurationClass.writeFile()
      dataStrategyConfigurationClass.defrost()

      println("源文件的地址:" + dataStrategyConfigurationClass.getClassFile().getSourceFile())
    }
  }

  private
  static void injectMethodCall(String methodName2Inject, Set<String> params, CtMethod ctMethod) {
    params.each {String param ->
      ctMethod.insertAfter("$methodName2Inject(\"$param\");")

      println("insert after:" + "$methodName2Inject(\"$param\");")
    }
  }
}
