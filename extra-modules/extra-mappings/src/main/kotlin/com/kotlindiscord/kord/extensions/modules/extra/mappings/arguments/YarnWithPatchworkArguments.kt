/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package com.kotlindiscord.kord.extensions.modules.extra.mappings.arguments

import com.kotlindiscord.kord.extensions.commands.application.slash.converters.impl.optionalEnumChoice
import com.kotlindiscord.kord.extensions.modules.extra.mappings.enums.YarnChannels
import me.shedaniel.linkie.namespaces.YarnNamespace

/** Arguments for Yarn mappings lookup commands when the Patchwork channel is enabled. **/
@Suppress("UndocumentedPublicProperty")
class YarnWithPatchworkArguments : MappingWithChannelArguments(YarnNamespace) {
    override val channel by optionalEnumChoice<YarnChannels> {
        name = "channel"
        description = "Mappings channel to use for this query"

        typeName = "official/snapshot/patchwork"
    }
}
