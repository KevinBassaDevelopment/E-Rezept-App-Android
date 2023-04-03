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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.gematik.ti.erp.app.R
import de.gematik.ti.erp.app.prescription.detail.ui.model.PrescriptionData
import de.gematik.ti.erp.app.theme.AppTheme
import de.gematik.ti.erp.app.theme.PaddingDefaults
import de.gematik.ti.erp.app.utils.compose.SpacerMedium
import de.gematik.ti.erp.app.utils.compose.SpacerSmall
import de.gematik.ti.erp.app.utils.dateTimeMediumText
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

sealed class PrescriptionDetailBottomSheetContent {
    @Stable
    class HowLongValid(val prescription: PrescriptionData.Synced) : PrescriptionDetailBottomSheetContent()

    object SubstitutionAllowed : PrescriptionDetailBottomSheetContent()
    object DirectAssignment : PrescriptionDetailBottomSheetContent()
    object EmergencyFee : PrescriptionDetailBottomSheetContent()
    object EmergencyFeeNotExempt : PrescriptionDetailBottomSheetContent()

    object AdditionalFeeNotExempt : PrescriptionDetailBottomSheetContent()
    object AdditionalFeeExempt : PrescriptionDetailBottomSheetContent()
    object Scanned : PrescriptionDetailBottomSheetContent()
    object Failure : PrescriptionDetailBottomSheetContent()
}

@Composable
fun PrescriptionDetailInfoSheetContent(
    infoContent: PrescriptionDetailBottomSheetContent
) {
    when (infoContent) {
        PrescriptionDetailBottomSheetContent.DirectAssignment ->
            PrescriptionDetailInfoSheetContent(
                title = stringResource(R.string.pres_details_exp_da_title),
                info = stringResource(R.string.pres_details_exp_da_info)
            )

        PrescriptionDetailBottomSheetContent.EmergencyFee ->
            PrescriptionDetailInfoSheetContent(
                title = stringResource(R.string.pres_details_exp_em_fee_title),
                info = stringResource(R.string.pres_details_exp_em_fee_info)
            )

        PrescriptionDetailBottomSheetContent.EmergencyFeeNotExempt ->
            PrescriptionDetailInfoSheetContent(
                title = stringResource(R.string.pres_details_exp_no_em_fee_title),
                info = stringResource(R.string.pres_details_exp_no_em_fee_info)
            )

        PrescriptionDetailBottomSheetContent.AdditionalFeeNotExempt ->
            PrescriptionDetailInfoSheetContent(
                title = stringResource(R.string.pres_details_exp_add_fee_title),
                info = stringResource(R.string.pres_details_exp_add_fee_info)
            )

        PrescriptionDetailBottomSheetContent.AdditionalFeeExempt ->
            PrescriptionDetailInfoSheetContent(
                title = stringResource(R.string.pres_details_exp_no_add_fee_title),
                info = stringResource(R.string.pres_details_exp_no_add_fee_info)
            )

        is PrescriptionDetailBottomSheetContent.HowLongValid ->
            PrescriptionDetailInfoSheetContent(
                title = stringResource(R.string.pres_details_exp_valid_title)
            ) {
                val start = if (infoContent.prescription.medicationRequest.multiplePrescriptionInfo.indicator) {
                    infoContent.prescription.medicationRequest.multiplePrescriptionInfo.start
                        ?: infoContent.prescription.authoredOn
                } else {
                    infoContent.prescription.authoredOn
                }
                Column {
                    DateRange(start = start, end = infoContent.prescription.acceptUntil ?: start)
                    SpacerSmall()
                    Text(
                        stringResource(R.string.pres_details_exp_valid_info_accept),
                        style = AppTheme.typography.body2l
                    )
                    if (!infoContent.prescription.medicationRequest.multiplePrescriptionInfo.indicator) {
                        SpacerMedium()
                        DateRange(
                            start = remember {
                                infoContent.prescription.acceptUntil?.plus(1.days) ?: start
                            },
                            end = infoContent.prescription.expiresOn ?: start
                        )
                        SpacerSmall()
                        Text(
                            stringResource(R.string.pres_details_exp_valid_info_expires),
                            style = AppTheme.typography.body2l
                        )
                    }
                }
            }

        PrescriptionDetailBottomSheetContent.SubstitutionAllowed ->
            PrescriptionDetailInfoSheetContent(
                title = stringResource(R.string.pres_details_exp_sub_allowed_title),
                info = stringResource(R.string.pres_details_exp_sub_allowed_info)
            )

        is PrescriptionDetailBottomSheetContent.Scanned ->
            PrescriptionDetailInfoSheetContent(
                title = stringResource(R.string.pres_details_exp_scanned_title),
                info = stringResource(R.string.pres_details_exp_scanned_info)
            )

        PrescriptionDetailBottomSheetContent.Failure ->
            PrescriptionDetailInfoSheetContent(
                title = stringResource(R.string.pres_details_exp_failure_title),
                info = stringResource(R.string.pres_details_exp_failure_info)
            )
    }
}

@Composable
private fun DateRange(start: Instant, end: Instant) {
    val startText = remember { dateTimeMediumText(start) }
    val endText = remember { dateTimeMediumText(end) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(PaddingDefaults.Small)
    ) {
        Text(startText, style = AppTheme.typography.subtitle2l)
        Icon(Icons.Rounded.ArrowForward, null, tint = AppTheme.colors.primary600, modifier = Modifier.size(16.dp))
        Text(endText, style = AppTheme.typography.subtitle2l)
    }
}

@Composable
private fun PrescriptionDetailInfoSheetContent(
    title: String,
    info: String
) {
    PrescriptionDetailInfoSheetContent(
        title = title
    ) {
        Text(info, style = AppTheme.typography.body2l)
    }
}

@Composable
private fun PrescriptionDetailInfoSheetContent(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = PaddingDefaults.Medium)
            .padding(top = PaddingDefaults.Small, bottom = PaddingDefaults.XXLarge)
    ) {
        Icon(
            Icons.Rounded.DragHandle,
            null,
            tint = AppTheme.colors.neutral600,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        SpacerMedium()
        Text(title, style = AppTheme.typography.subtitle1)
        SpacerMedium()
        Box(Modifier.verticalScroll(rememberScrollState())) {
            content()
        }
    }
}
