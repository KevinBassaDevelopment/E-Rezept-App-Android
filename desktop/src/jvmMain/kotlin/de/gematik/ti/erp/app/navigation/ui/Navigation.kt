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

package de.gematik.ti.erp.app.navigation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

@Stable
class Navigation(startDestination: Destination) {
    private val observableBackStack = mutableStateListOf(startDestination)

    val backStackEntries: List<Destination>
        get() = observableBackStack

    val currentBackStackEntry: Destination?
        get() = observableBackStack.lastOrNull()

    fun back() {
        if (observableBackStack.isNotEmpty()) {
            observableBackStack.removeLast()
        }
    }

    fun navigate(destination: Destination) {
        observableBackStack.add(destination)
    }

    fun navigate(destination: Destination, clearBackStack: Boolean) {
        if (clearBackStack) {
            observableBackStack.clear()
        }
        observableBackStack.add(destination)
    }
}

interface Destination

@Composable
fun rememberNavigation(startDestination: Destination): Navigation {
    return remember { Navigation(startDestination) }
}
