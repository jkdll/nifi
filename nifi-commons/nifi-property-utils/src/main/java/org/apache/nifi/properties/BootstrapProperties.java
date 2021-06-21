/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.properties;

import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * Properties representing bootstrap.conf.
 */
public class BootstrapProperties extends StandardReadableProperties {
    private static final String PROPERTY_KEY_FORMAT = "%s.%s";
    private static final String BOOTSTRAP_SENSITIVE_KEY = "bootstrap.sensitive.key";

    private final String propertyPrefix;
    private final Path configFilePath;

    public BootstrapProperties(final String propertyPrefix, final Properties properties, final Path configFilePath) {
        super(new Properties());

        Objects.requireNonNull(properties, "Properties are required");
        this.propertyPrefix = Objects.requireNonNull(propertyPrefix, "Property prefix is required");
        this.configFilePath = configFilePath;

        this.filterProperties(properties);

    }

    /**
     * Returns the path to the bootstrap config file.
     * @return The path to the file
     */
    public Path getConfigFilePath() {
        return configFilePath;
    }

    /**
     * Includes only the properties starting with the propertyPrefix.
     * @param properties Unfiltered properties
     */
    private void filterProperties(final Properties properties) {
        getRawProperties().clear();
        final Properties filteredProperties = new Properties();
        for(final Enumeration<Object> e = properties.keys() ; e.hasMoreElements(); ) {
            final String key = e.nextElement().toString();
            if (key.startsWith(propertyPrefix)) {
                filteredProperties.put(key, properties.getProperty(key));
            }
        }
        getRawProperties().putAll(filteredProperties);
    }

    private String getPropertyKey(final String subKey) {
        return String.format(PROPERTY_KEY_FORMAT, propertyPrefix, subKey);
    }

    /**
     * Returns the bootstrap sensitive key.
     * @return The bootstrap sensitive key
     */
    public Optional<String> getBootstrapSensitiveKey() {
        return Optional.ofNullable(getProperty(getPropertyKey(BOOTSTRAP_SENSITIVE_KEY)));
    }

    @Override
    public String toString() {
        return String.format("Bootstrap properties [%s] with prefix [%s]", configFilePath, propertyPrefix);
    }
}