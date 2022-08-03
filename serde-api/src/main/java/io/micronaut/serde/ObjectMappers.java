/*
 * Copyright 2017-2022 original authors
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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import io.micronaut.context.ApplicationContextConfiguration;
import io.micronaut.context.BeanContext;
import io.micronaut.context.DefaultApplicationContext;
import io.micronaut.context.env.DefaultEnvironment;
import io.micronaut.context.env.Environment;
import io.micronaut.inject.BeanDefinitionReference;

/**
 *
 * @author graemerocher
 * @since 1.2.2
 */
@SuppressWarnings("java:S3077")
final class ObjectMappers {
    private static volatile ObjectMapper defaultObjectMapper;
    private static volatile BeanContext beanContext;
    private static final Object MAPPER_LOCK = new Object();
    private static final Object CONTEXT_LOCK = new Object();

    private ObjectMappers() {
    }

    /**
     * Resolves the default.
     * @return The object mapper
     */
    @SuppressWarnings("java:S2095")
    static ObjectMapper resolveDefault() {
        ObjectMapper objectMapper = defaultObjectMapper;
        if (objectMapper == null) {
            synchronized (MAPPER_LOCK) {
                objectMapper = defaultObjectMapper;
                if (objectMapper == null) {
                    objectMapper = resolveBeanContext().getBean(ObjectMapper.class);
                    defaultObjectMapper = objectMapper;
                }
            }
        }
        return objectMapper;
    }

    @SuppressWarnings("java:S2095")
    private static BeanContext resolveBeanContext() {
        BeanContext context = beanContext;
        if (context == null) {
            synchronized (CONTEXT_LOCK) {
                context = beanContext;
                if (context == null) {
                    context = new ObjectMapperContext().start();
                    beanContext = context;
                }
            }
        }
        return context;
    }

    private static class ObjectMapperContext extends DefaultApplicationContext {

        @Override
        protected List<BeanDefinitionReference> resolveBeanDefinitionReferences() {
            return super.resolveBeanDefinitionReferences()
                .stream()
                .filter(ref ->
                    ref.getBeanDefinitionName().startsWith("io.micronaut.serde") ||
                    ref.getBeanDefinitionName().startsWith("io.micronaut.aop") ||
                    ref.getBeanDefinitionName().startsWith("io.micronaut.runtime.context.env")
                )
                .collect(Collectors.toList());
        }

        @Override
        @SuppressWarnings("java:S1874")
        public Future<Void> publishEventAsync(Object event) {
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public void publishEvent(Object event) {
            // no-op
        }

        @Override
        protected Environment createEnvironment(ApplicationContextConfiguration configuration) {
            return new DefaultEnvironment((ApplicationContextConfiguration) getContextConfiguration()) {
                @Override
                protected void readPropertySources(String name) {
                    // no-op
                }
            };
        }
    }
}