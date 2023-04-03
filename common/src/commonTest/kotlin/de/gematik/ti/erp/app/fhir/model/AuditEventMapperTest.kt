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

@file:Suppress("ktlint:max-line-length")

package de.gematik.ti.erp.app.fhir.model

import de.gematik.ti.erp.app.fhir.parser.asFhirTemporal
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

private val testBundle by lazy {
    File("$ResourceBasePath/audit_events_bundle.json").readText()
}
private val testAuditEventVersion12 by lazy {
    File("$ResourceBasePath/audit_events_bundle_version_1_2.json").readText()
}

class AuditEventMapperTest {
    private class AuditEvent(
        val id: String,
        val taskId: String?,
        val description: String,
        val timestamp: Instant
    )

    @Suppress("MaxLineLength")
    private val events = mapOf(
        0 to AuditEvent(
            id = "01eb7f56-6820-a140-abdb-34aa9f2ab6ea",
            taskId = null,
            description = "Zacharias Zebra hat eine Liste mit Medikament-Informationen heruntergeladen.",
            timestamp = Instant.parse("2022-01-13T15:44:15.816+00:00")
        ),
        2 to AuditEvent(
            id = "01eb7f56-75dc-6850-9729-d94c0839ab3b",
            taskId = "169.000.000.000.026.84",
            description = "Praxis Rainer Graf d' AgóstinoTEST-ONLY hat das Rezept mit der ID 169.000.000.000.026.84 eingestellt.",
            timestamp = Instant.parse("2022-01-13T15:48:06.226+00:00")
        ),
        7 to AuditEvent(
            id = "01eb7f56-862a-e830-e470-120f0137c54e",
            taskId = "169.000.000.000.026.84",
            description = "Zacharias Zebra hat das Rezept mit der ID 169.000.000.000.026.84 heruntergeladen.",
            timestamp = Instant.parse("2022-01-13T15:52:39.806+00:00")
        )
    )

    private val auditEventsVersion12 = mapOf(
        0 to AuditEvent(
            id = "9361863d-fec0-4ba9-8776-7905cf1b0cfa",
            taskId = null,
            description = "Praxis Dr. Müller, Bahnhofstr. 78 hat ein E-Rezept 160.123.456.789.123.58 eingestellt",
            timestamp = Instant.parse("2022-04-27T08:04:27.434Z")
        )
    )

    @Test
    fun `parse audit events`() {
        var index = 0

        extractAuditEvents(
            Json.parseToJsonElement(testBundle)
        ) { id, taskId, description, timestamp ->
            events[index]?.let { ev ->
                assertEquals(ev.id, id)
                assertEquals(ev.taskId, taskId)
                assertEquals(ev.description, description)
                assertEquals(ev.timestamp, timestamp.value)
            }

            index++
        }

        assertEquals(50, index)
    }

    @Test
    fun `parse audit events version 1_2`() {
        extractAuditEvents(
            Json.parseToJsonElement(testAuditEventVersion12)
        ) { id, taskId, description, timestamp ->
            auditEventsVersion12[0]?.let { ev ->
                assertEquals(ev.id, id)
                assertEquals(ev.taskId, taskId)
                assertEquals(ev.description, description)
                assertEquals(ev.timestamp.asFhirTemporal(), timestamp)
            }
        }
    }
}
