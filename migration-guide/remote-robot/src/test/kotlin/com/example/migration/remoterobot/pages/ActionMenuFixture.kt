package com.example.migration.remoterobot.pages

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.ComponentFixture
import com.intellij.remoterobot.fixtures.ContainerFixture
import com.intellij.remoterobot.fixtures.FixtureName
import com.intellij.remoterobot.search.locators.byXpath

/**
 * The library has JPopupMenuFixture for the popup itself, but nothing for a single entry and no
 * way to read its label, so both the locator and the getter are written here. Driver ships
 * PopupItemUiComponent with getText().
 *
 * Two class names, because @class is the simple runtime class name: a leaf is an ActionMenuItem
 * and a submenu is an ActionMenu, and neither name covers both.
 */
fun ContainerFixture.allMenuItems(): List<MenuEntryFixture> =
  findAll(byXpath("all menu items", "//div[@class='ActionMenuItem' or @class='ActionMenu']"))

/** Only the entries that open a submenu. */
fun ContainerFixture.allSubmenus(): List<MenuEntryFixture> =
  findAll(byXpath("all submenus", "//div[@class='ActionMenu']"))

@FixtureName("Menu entry")
class MenuEntryFixture(remoteRobot: RemoteRobot, remoteComponent: RemoteComponent) :
  ComponentFixture(remoteRobot, remoteComponent) {

  val text: String
    get() = callJs("component.getText()", true)
}
