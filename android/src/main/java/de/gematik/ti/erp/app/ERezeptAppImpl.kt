/*
 * ${GEMATIK_COPYRIGHT_STATEMENT}
 */

/*
 * Urheberrechtshinweis: Diese Software ist urheberrechtlich geschützt. Das Urheberrecht liegt bei
 * Research Industrial Systems Engineering (RISE) Forschungs-, Entwicklungs- und Großprojektberatung GmbH,
 * soweit nicht im Folgenden näher gekennzeichnet.
 */

package de.gematik.ti.erp.app

import android.content.Context
import android.content.Intent

/**
 * Implementation of the [ERezeptApp] interface.
 *
 * @author RISE GmbH
 */
internal class ERezeptAppImpl(private val context: Context) : ERezeptApp {

    override fun open() {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
}
