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

package de.gematik.ti.erp.app.communication.repository

import de.gematik.ti.erp.app.prescription.repository.SimpleCommunication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class LocalDataSource {
    private val communications = MutableStateFlow(emptyList<SimpleCommunication>())
    private val lock = Mutex()

    suspend fun saveCommunications(communications: List<SimpleCommunication>) = lock.withLock {
        val ids = communications.map { it.id }
        this.communications.value = this.communications.value.filter { it.id !in ids } + communications
    }

    fun loadCommunications(): Flow<List<SimpleCommunication>> {
        return communications
    }

    suspend fun invalidate() = lock.withLock {
        communications.value = emptyList()
    }
}
