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

package de.gematik.ti.erp.app.cardwall.usecase.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

object NfcPositionUseCaseData {
    @Immutable
    @Serializable
    data class NfcData(
        val manufacturer: String,
        val marketingName: String,
        val modelNames: List<String>,
        val nfcPos: NfcPos
    )

    @Immutable
    @Serializable
    data class NfcPos(
        val x0: Double,
        val y0: Double,
        val x1: Double,
        val y1: Double
    )
}
