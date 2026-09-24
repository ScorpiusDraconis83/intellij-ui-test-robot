package com.example.migration.starter.pages

import com.intellij.driver.sdk.ui.components.ComponentData
import com.intellij.driver.sdk.ui.components.UiComponent
import com.intellij.driver.sdk.ui.components.common.IdeaFrameUI
import com.intellij.driver.sdk.ui.components.elements.JTreeUiComponent
import com.intellij.driver.sdk.ui.components.elements.tree
import com.intellij.driver.sdk.ui.xQuery

/** A wrapper is an ordinary class plus one function that knows how to find it. */
// 1. The class.
class ProjectPanelUi(data: ComponentData) : UiComponent(data) {

  // 2. Single children are fields. x() only builds an object holding a query, and that object
  // searches for itself the first time the test acts on it.
  val tree: JTreeUiComponent = tree(xQuery { byType("com.intellij.ide.projectView.impl.ProjectViewTree") })

  // 3. Collections do not wait. xx().list() is a single findAll() with no retry, so the caller
  // decides how long to wait.
  private val contentLabels = xx { byClass("ContentComboLabel") }

  fun contentLabels(): List<UiComponent> = contentLabels.list()
}

// 4. The function that finds it. By type, not by an xpath string. The receiver says where the
// panel lives, so the call is only offered where it can work.
fun IdeaFrameUI.projectPanel(): ProjectPanelUi =
  x(ProjectPanelUi::class.java) {
    componentWithChild(
      byType("com.intellij.toolWindow.InternalDecoratorImpl"),
      byType("com.intellij.ide.projectView.impl.ProjectViewTree")
    )
  }
