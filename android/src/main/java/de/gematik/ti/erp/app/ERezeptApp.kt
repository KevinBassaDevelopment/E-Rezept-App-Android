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
import androidx.annotation.Keep

/**
 * E-Rezept App.
 *
 * @author RISE GmbH
 */
interface ERezeptApp {

    companion object {

        @Keep
        const val version: String = "1.0.0"

        /**
         * Creates an instance of the MioViewer.
         *
         * @param context [Context] to be used to.
         */
        fun create(
            context: Context
        ): ERezeptApp {
            return ERezeptAppImpl(context)
        }
    }

    /**
     * Opens a E-Rezept in an activity.
     */
    fun open()
}
