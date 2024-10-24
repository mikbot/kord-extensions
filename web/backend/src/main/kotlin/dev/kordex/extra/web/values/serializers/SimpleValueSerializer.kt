/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

@file:Suppress("MagicNumber")

package dev.kordex.extra.web.values.serializers

import dev.kord.common.entity.optional.Optional
import dev.kordex.extra.web.types.Identifier
import dev.kordex.extra.web.values.types.SimpleValue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

public class SimpleValueSerializer<T : Any>(
	private val dataSerializer: KSerializer<T>,
) : KSerializer<SimpleValue<T>> {
	private val optionalSerializer = Optional.serializer(dataSerializer.nullable)

	override val descriptor: SerialDescriptor =
		buildClassSerialDescriptor("SimpleValue", dataSerializer.descriptor) {
			element<Identifier>("identifier")
			element<Boolean>("writable")
			element("value", optionalSerializer.descriptor)
		}

	override fun deserialize(decoder: Decoder): SimpleValue<T> {
		var identifier = Identifier("", "")
		var writeable = true
		var value: Optional<T?> = Optional.Missing()

		decoder.decodeStructure(descriptor) {
			while (true) {
				when (val index = decodeElementIndex(descriptor)) {
					1 -> identifier = decodeSerializableElement(descriptor, index, Identifier.serializer())
					2 -> writeable = decodeBooleanElement(descriptor, index)
					3 -> value = decodeSerializableElement(descriptor, index, optionalSerializer)

					CompositeDecoder.DECODE_DONE -> break

					else -> error("Unexpected descriptor index: $index")
				}
			}
		}

		val result = SimpleValue(identifier, writeable, dataSerializer)

		result.writeOptional(value)

		return result
	}

	override fun serialize(encoder: Encoder, value: SimpleValue<T>) {
		encoder.encodeStructure(descriptor) {
			encodeSerializableElement(descriptor, 0, Identifier.serializer(), value.identifier)
			encodeBooleanElement(descriptor, 1, value.writable)
			encodeSerializableElement(descriptor, 2, optionalSerializer, value.optional)
		}
	}
}
