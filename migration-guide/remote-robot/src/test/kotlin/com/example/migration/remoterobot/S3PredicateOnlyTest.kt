package com.example.migration.remoterobot

import com.example.migration.remoterobot.pages.allMenuItems
import com.example.migration.remoterobot.pages.allSubmenus
import com.example.migration.remoterobot.pages.idea
import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.fixtures.JPopupMenuFixture
import com.intellij.remoterobot.utils.keyboard
import com.intellij.remoterobot.utils.waitForIgnoringError
import org.junit.jupiter.api.Test
import java.time.Duration

/**
 * S3. Pick an entry that only a predicate can identify.
 *
 * The editor context menu holds two entries with the word Paste, the leaf Paste and the submenu
 * Copy / Paste Special, and only the component class separates them. Exact labels are no help
 * either: Rename… ends in a real ellipsis character and Run 'Main.main()' is built at runtime.
 */
class S3PredicateOnlyTest : RemoteRobotScenarioTest() {

  @Test
  fun pickEntryOnlyAPredicateCanIdentify(remoteRobot: RemoteRobot) = with(remoteRobot) {
    openSampleProject(this)

    idea {
      openFile("src/Main.java")
      // textEditor() carries its own five second search, so wait for the same component.
      waitForIgnoringError(
        Duration.ofSeconds(180),
        description = "the editor to open",
        errorMessage = "no editor ever appeared for src/Main.java"
      ) {
        textEditors().isNotEmpty()
      }
      bringToFront()

      // 1. Open the context menu with a real right click.
      textEditor(Duration.ofSeconds(90)).editor.rightClick()
    }

    // 2. Take the popup. The popup is its own window rather than a part of the IDE frame, so the
    // search starts from remoteRobot.
    val menu = find<JPopupMenuFixture>(JPopupMenuFixture.byType(), Duration.ofSeconds(30))

    // 3. Two collections, both scoped to the popup. A single query already handles both classes,
    // so the second one is not there for coverage. Step 4 needs exactly one candidate, and only
    // the submenus give it one. They live in pages/ActionMenuFixture.kt.

    // 4. Poll the submenus with a predicate.
    waitForIgnoringError(Duration.ofSeconds(90), description = "the Paste submenu") {
      menu.allSubmenus().count { it.text.contains("Paste") } == 1
    }
    val pasteSpecial = menu.allSubmenus().single { it.text.contains("Paste") }

    // Taken before the click, so step 6 has something to compare against.
    val entryCount = menu.allMenuItems().size

    // 5. Click it. menu.select() is shorter, but it needs one exact label, and it would find the
    // leaf Paste rather than the submenu.
    pasteSpecial.click()

    // 6. It opened a submenu, so there are more entries on screen than before.
    waitForIgnoringError(Duration.ofSeconds(45), description = "the submenu to open") {
      menu.allMenuItems().size > entryCount
    }

    // 7. A submenu is open on top of the menu, so two escapes.
    keyboard { escape(); escape() }
  }
}
