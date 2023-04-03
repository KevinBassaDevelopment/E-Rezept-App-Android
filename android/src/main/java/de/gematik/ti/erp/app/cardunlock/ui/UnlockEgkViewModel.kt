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

package de.gematik.ti.erp.app.cardunlock.ui

import android.nfc.Tag
import de.gematik.ti.erp.app.DispatchProvider
import de.gematik.ti.erp.app.cardunlock.usecase.UnlockEgkState
import de.gematik.ti.erp.app.cardunlock.usecase.UnlockEgkUseCase
import de.gematik.ti.erp.app.cardwall.model.nfc.card.NfcHealthCard
import androidx.lifecycle.ViewModel
import de.gematik.ti.erp.app.card.model.command.UnlockMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class UnlockEgkViewModel(
    private val unlockEgkUseCase: UnlockEgkUseCase,
    private val dispatchers: DispatchProvider
) : ViewModel() {
    fun unlockEgk(
        unlockMethod: UnlockMethod,
        can: String,
        puk: String,
        oldSecret: String,
        newSecret: String,
        tag: Flow<Tag>
    ): Flow<UnlockEgkState> {
        val cardChannel = tag.map { NfcHealthCard.connect(it) }
        return unlockEgkUseCase.unlockEgk(
            unlockMethod = unlockMethod,
            can = can,
            puk = puk,
            oldSecret = oldSecret,
            newSecret = newSecret,
            cardChannel = cardChannel
        ).flowOn(dispatchers.IO)
    }
}
