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

package de.gematik.ti.erp.app.idp.repository

import de.gematik.ti.erp.app.idp.model.IdpData
import de.gematik.ti.erp.app.profiles.repository.ProfileIdentifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class IdpPairingRepository constructor(
    private val localDataSource: IdpLocalDataSource
) {
    private val decryptedAccessTokenMap: MutableStateFlow<Map<ProfileIdentifier, String>> =
        MutableStateFlow(mutableMapOf())
    private val singleSignOnTokenMap: MutableStateFlow<Map<ProfileIdentifier, IdpData.SingleSignOnToken>> =
        MutableStateFlow(mutableMapOf())

    fun decryptedAccessToken(profileId: ProfileIdentifier) =
        decryptedAccessTokenMap.map { it[profileId] }.distinctUntilChanged()

    fun saveDecryptedAccessToken(profileId: ProfileIdentifier, accessToken: String) {
        decryptedAccessTokenMap.update {
            it + (profileId to accessToken)
        }
    }

    fun invalidateDecryptedAccessToken(profileId: ProfileIdentifier) {
        decryptedAccessTokenMap.update {
            it - profileId
        }
    }

    /**
     * This function fuses the scope of the original prescription token with the token scoped to pairing.
     */
    fun singleSignOnTokenScope(profileId: ProfileIdentifier) =
        combine(
            localDataSource.authenticationData(profileId),
            singleSignOnTokenMap
                .map { it[profileId] }
                .distinctUntilChanged()
        ) { authData, pairingToken ->
            when (val originalToken = authData.singleSignOnTokenScope) {
                is IdpData.ExternalAuthenticationToken ->
                    pairingToken?.let {
                        IdpData.ExternalAuthenticationToken(
                            token = it,
                            authenticatorId = originalToken.authenticatorId,
                            authenticatorName = originalToken.authenticatorName
                        )
                    }
                is IdpData.AlternateAuthenticationToken ->
                    pairingToken?.let {
                        IdpData.AlternateAuthenticationToken(
                            token = it,
                            cardAccessNumber = originalToken.cardAccessNumber,
                            aliasOfSecureElementEntry = originalToken.aliasOfSecureElementEntry,
                            healthCardCertificate = originalToken.healthCardCertificate
                        )
                    }
                is IdpData.AlternateAuthenticationWithoutToken ->
                    if (pairingToken == null) {
                        originalToken
                    } else {
                        IdpData.AlternateAuthenticationToken(
                            token = pairingToken,
                            cardAccessNumber = originalToken.cardAccessNumber,
                            aliasOfSecureElementEntry = originalToken.aliasOfSecureElementEntry,
                            healthCardCertificate = originalToken.healthCardCertificate
                        )
                    }
                is IdpData.DefaultToken ->
                    pairingToken?.let {
                        IdpData.DefaultToken(
                            token = it,
                            cardAccessNumber = originalToken.cardAccessNumber,
                            healthCardCertificate = originalToken.healthCardCertificate
                        )
                    }
                null -> null
            }
        }

    fun saveSingleSignOnToken(profileId: ProfileIdentifier, token: IdpData.SingleSignOnToken) {
        singleSignOnTokenMap.update {
            it + (profileId to token)
        }
    }

    fun invalidateSingleSignOnToken(profileId: ProfileIdentifier) {
        singleSignOnTokenMap.update {
            it - profileId
        }
    }
}
