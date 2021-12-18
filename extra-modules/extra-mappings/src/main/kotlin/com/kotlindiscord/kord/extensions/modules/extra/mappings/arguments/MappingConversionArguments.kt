/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.kotlindiscord.kord.extensions.modules.extra.mappings.arguments

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.converters.impl.stringChoice
import com.kotlindiscord.kord.extensions.commands.converters.impl.optionalString
import com.kotlindiscord.kord.extensions.commands.converters.impl.string

/**
 * Arguments for class, field, and method conversion commands.
 */
@Suppress("UndocumentedPublicProperty")
class MappingConversionArguments(enabledNamespaces: Map<String, String>) : Arguments() {
    val query by string("query", "Name to query mappings for")
    val inputNamespace by stringChoice("input", "The namespace to convert from", enabledNamespaces)
    val outputNamespace by stringChoice("output", "The namespace to convert to", enabledNamespaces)
    val version by optionalString(
        "version",
        "Minecraft version to use for this query",
    )

    val inputChannel by optionalString("inputChannel", "The mappings channel to use for input")
    val outputChannel by optionalString("outputChannel", "The mappings channel to use for output")
}