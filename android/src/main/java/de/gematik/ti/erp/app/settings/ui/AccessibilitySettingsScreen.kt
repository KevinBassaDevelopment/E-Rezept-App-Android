/*
 * ${GEMATIK_COPYRIGHT_STATEMENT}
 */

/*
 * Copyright (c) 2023 gematik GmbH
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the Licence);
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *     https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 */

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.ZoomIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import de.gematik.ti.erp.app.R
import de.gematik.ti.erp.app.settings.ui.SettingsController
import de.gematik.ti.erp.app.utils.compose.AcceptDialog
import de.gematik.ti.erp.app.utils.compose.AnimatedElevationScaffold
import de.gematik.ti.erp.app.utils.compose.LabeledSwitch
import de.gematik.ti.erp.app.utils.compose.NavigationBarMode
import de.gematik.ti.erp.app.utils.compose.SpacerMedium
import kotlinx.coroutines.launch

@Composable
fun AccessibilitySettingsScreen(settingsController: SettingsController, onBack: () -> Unit) {
    val zoomState by settingsController.zoomState
    val screenshotState by settingsController.screenShotState
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var showAllowScreenShotsAlert by remember { mutableStateOf(false) }

    AnimatedElevationScaffold(
        topBarTitle = stringResource(R.string.settings_accessibility_headline),
        navigationMode = NavigationBarMode.Back,
        listState = listState,
        onBack = onBack
    ) {
        LazyColumn(
            contentPadding = it,
            state = listState
        ) {
            item {
                SpacerMedium()
                ZoomSection(zoomChecked = zoomState.zoomEnabled) { zoomEnabled ->
                    when (zoomEnabled) {
                        true -> scope.launch {
                            settingsController.onEnableZoom()
                        }
                        false -> scope.launch {
                            settingsController.onDisableZoom()
                        }
                    }
                }
            }
            item {
                AllowScreenShotsSection(
                    screenshotState.screenshotsAllowed
                ) { screenShotsAllowed ->
                    settingsController.onSwitchAllowScreenshots(screenShotsAllowed)
                    showAllowScreenShotsAlert = true
                }
            }
        }
        if (showAllowScreenShotsAlert) {
            RestartAlert { showAllowScreenShotsAlert = false }
        }
    }
}

@Composable
private fun ZoomSection(
    modifier: Modifier = Modifier,
    zoomChecked: Boolean,
    onZoomChange: (Boolean) -> Unit
) {
    LabeledSwitch(
        modifier = modifier,
        checked = zoomChecked,
        onCheckedChange = onZoomChange,
        icon = Icons.Rounded.ZoomIn,
        header = stringResource(R.string.settings_accessibility_zoom_toggle),
        description = stringResource(R.string.settings_accessibility_zoom_info)
    )
}

@Composable
private fun AllowScreenShotsSection(
    allowScreenshots: Boolean,
    modifier: Modifier = Modifier,
    onAllowScreenshotsChange: (Boolean) -> Unit
) {
    LabeledSwitch(
        modifier = modifier,
        checked = !allowScreenshots,
        onCheckedChange = {
            onAllowScreenshotsChange(!it)
        },
        icon = Icons.Rounded.Camera,
        header = stringResource(R.string.settings_screenshots_toggle_text),
        description = stringResource(R.string.settings_screenshots_description)
    )
}

@Composable
private fun RestartAlert(onDismissRequest: () -> Unit) {
    val title = stringResource(R.string.settings_screenshots_alert_headline)
    val message = stringResource(R.string.settings_screenshots_alert_info)
    val confirmText = stringResource(R.string.settings_screenshots_button_text)

    AcceptDialog(
        header = title,
        onClickAccept = onDismissRequest,
        info = message,
        acceptText = confirmText
    )
}
