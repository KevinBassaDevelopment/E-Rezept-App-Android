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

package de.gematik.ti.erp.app.prescription.detail.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import de.gematik.ti.erp.app.R
import de.gematik.ti.erp.app.prescription.model.SyncedTaskData
import de.gematik.ti.erp.app.utils.compose.AnimatedElevationScaffold
import de.gematik.ti.erp.app.utils.compose.NavigationBarMode
import de.gematik.ti.erp.app.utils.compose.SpacerMedium

@Composable
fun IngredientScreen(ingredient: SyncedTaskData.Ingredient, onBack: () -> Unit) {
    val scaffoldState = rememberScaffoldState()
    val listState = rememberLazyListState()

    AnimatedElevationScaffold(
        scaffoldState = scaffoldState,
        listState = listState,
        onBack = onBack,
        topBarTitle = stringResource(R.string.synced_medication_ingredient_header),
        navigationMode = NavigationBarMode.Back,
        snackbarHost = { SnackbarHost(it, modifier = Modifier.navigationBarsPadding()) },
        actions = {}
    ) { innerPadding ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize().padding(innerPadding),
            contentPadding = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom).asPaddingValues()
        ) {
            item {
                SpacerMedium()
                IngredientNameLabel(ingredient.text)
            }

            ingredient.amount?.let {
                item {
                    IngredientAmountLabel(it)
                }
            }
            ingredient.number?.let {
                item {
                    IngredientNumberLabel(it)
                }
            }

            ingredient.form?.let {
                item {
                    FormLabel(it)
                }
            }

            ingredient.strength?.let {
                item {
                    StrengthLabel(it)
                }
            }
            item {
                SpacerMedium()
            }
        }
    }
}
