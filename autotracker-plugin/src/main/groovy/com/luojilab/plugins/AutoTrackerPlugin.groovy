package com.luojilab.plugins

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AutoTrackerPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    def android = project.extensions.getByType(AppExtension)
    android.registerTransform(new AutoTrackerTransform(project))
  }
}