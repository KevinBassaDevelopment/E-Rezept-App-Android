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

package de.gematik.ti.erp.app.card.model.exchange

import de.gematik.ti.erp.app.card.model.card.CardKey
import de.gematik.ti.erp.app.card.model.card.ICardChannel
import de.gematik.ti.erp.app.card.model.card.PsoAlgorithm
import de.gematik.ti.erp.app.card.model.cardobjects.Df
import de.gematik.ti.erp.app.card.model.cardobjects.Mf
import de.gematik.ti.erp.app.card.model.command.HealthCardCommand
import de.gematik.ti.erp.app.card.model.command.executeSuccessfulOn
import de.gematik.ti.erp.app.card.model.command.manageSecEnvForSigning
import de.gematik.ti.erp.app.card.model.command.psoComputeDigitalSignature
import de.gematik.ti.erp.app.card.model.command.select
import de.gematik.ti.erp.app.card.model.identifier.ApplicationIdentifier

fun ICardChannel.signChallenge(challenge: ByteArray): ByteArray {
    HealthCardCommand.select(ApplicationIdentifier(Df.Esign.AID)).executeSuccessfulOn(this)

    HealthCardCommand.manageSecEnvForSigning(
        PsoAlgorithm.SIGN_VERIFY_ECDSA,
        CardKey(Mf.Df.Esign.PrK.ChAutE256.KID),
        true
    ).executeSuccessfulOn(this)

    return HealthCardCommand.psoComputeDigitalSignature(challenge)
        .executeSuccessfulOn(this)
        .apdu.data
}
