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

package de.gematik.ti.erp.app.di

import de.gematik.ti.erp.app.db.appSchemas
import de.gematik.ti.erp.app.db.openRealmWith
import de.gematik.ti.erp.app.secureRandomInstance
import de.gematik.ti.erp.app.utils.cleanupDbFiles
import io.realm.kotlin.Realm
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindings.Scope
import org.kodein.di.bindings.ScopeCloseable
import org.kodein.di.scoped
import org.kodein.di.singleton

private const val PassphraseSizeInBytes = 64

@JvmInline
value class ScopedRealm(val realm: Realm) : ScopeCloseable {
    override fun close() {
        realm.close()
        cleanupDbFiles().start()
    }
}

fun realmModule(scope: Scope<Any?>) = DI.Module("Realm Module") {
    bind {
        scoped(scope).singleton {
            ScopedRealm(
                openRealmWith(
                    schemas = appSchemas,
                    configuration = {
                        it.encryptionKey(generatePassPhrase())
                        it.directory(System.getProperty("java.io.tmpdir"))
                    }
                )
            )
        }
    }
}

private fun generatePassPhrase(): ByteArray {
    return ByteArray(PassphraseSizeInBytes).apply {
        secureRandomInstance().nextBytes(this)
    }
}
