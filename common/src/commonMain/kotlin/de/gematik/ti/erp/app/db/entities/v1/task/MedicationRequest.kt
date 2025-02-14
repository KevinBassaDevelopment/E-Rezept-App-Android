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

package de.gematik.ti.erp.app.db.entities.v1.task

import de.gematik.ti.erp.app.db.entities.Cascading
import de.gematik.ti.erp.app.db.entities.enumName
import io.realm.kotlin.Deleteable
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore

enum class AccidentTypeV1 {
    Unfall,
    Arbeitsunfall,
    Berufskrankheit,
    None
}

@Suppress("LongParameterList")
class MedicationRequestEntityV1 : RealmObject, Cascading {
    var medication: MedicationEntityV1? = null
    var dateOfAccident: RealmInstant? = null // unfalltag
    var location: String? = null // unfallbetrieb
    @delegate:Ignore
    var accidentType: AccidentTypeV1 by enumName(::_accidentType)
    var _accidentType: String = AccidentTypeV1.None.toString()
    var emergencyFee: Boolean? = null // emergency service fee = notfallgebuehr
    var substitutionAllowed: Boolean = false
    var dosageInstruction: String? = null
    var quantity: Int = 0
    var note: String? = null
    var multiplePrescriptionInfo: MultiplePrescriptionInfoEntityV1? = null
    var bvg: Boolean = false
    var additionalFee: String? = null

    override fun objectsToFollow(): Iterator<Deleteable> = iterator {
        medication?.let { yield(it) }
        multiplePrescriptionInfo?.let { yield(it) }
    }
}
