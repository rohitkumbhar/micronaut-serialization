/*
 * Copyright 2017-2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.serde;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.convert.ConversionServiceProvider;

/**
 * Represents a registry where specific serializers can be looked up.
 *
 * @author graemerocher
 * @since 1.0.0
 *
 */
public interface SerdeRegistry extends SerializerLocator, DeserializerLocator, NamingStrategyLocator, ConversionServiceProvider {

    /**
     * Creates a new encoder context.
     * @param view The view
     * @return The encoder context
     */
    @NonNull Serializer.EncoderContext newEncoderContext(@Nullable Class<?> view);

    /**
     * Creates a new decoder context.
     * @param view The view
     * @return The decoder context
     */
    @NonNull Deserializer.DecoderContext newDecoderContext(@Nullable Class<?> view);
}
