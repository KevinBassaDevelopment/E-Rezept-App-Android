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

package de.gematik.ti.erp.app.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.gematik.ti.erp.app.common.theme.PaddingDefaults

@Composable
fun SpacerLarge() =
    Spacer(modifier = Modifier.size(PaddingDefaults.Large))

@Composable
fun SpacerMedium() =
    Spacer(modifier = Modifier.size(PaddingDefaults.Medium))

@Composable
fun SpacerSmall() =
    Spacer(modifier = Modifier.size(PaddingDefaults.Small))

@Composable
fun SpacerTiny() =
    Spacer(modifier = Modifier.size(PaddingDefaults.Tiny))
