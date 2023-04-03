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

package de.gematik.ti.erp.app.redeem.ui.model

import de.gematik.ti.erp.app.Route

class RedeemNavigation {
    object HowToRedeem : Route("redeem_how_to")
    object PrescriptionSelection : Route("redeem_prescription_selection")
    object LocalRedeem : Route("redeem_local")
    object OnlineRedeem : Route("redeem_online")
    object PharmacySearch : Route("redeem_pharmacy_search")
}
