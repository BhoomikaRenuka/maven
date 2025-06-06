/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.cling.invoker.mvnenc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.maven.api.di.Inject;
import org.apache.maven.api.di.Named;
import org.apache.maven.api.di.Singleton;
import org.apache.maven.api.services.Prompter;
import org.apache.maven.api.services.PrompterException;
import org.codehaus.plexus.components.secdispatcher.MasterSource;
import org.codehaus.plexus.components.secdispatcher.MasterSourceMeta;
import org.codehaus.plexus.components.secdispatcher.SecDispatcher;
import org.codehaus.plexus.components.secdispatcher.SecDispatcherException;

/**
 * Trivial master password source using Maven {@link Prompter} service.
 */
@Singleton
@Named(ConsolePasswordPrompt.NAME)
public class ConsolePasswordPrompt implements MasterSource, MasterSourceMeta {
    public static final String NAME = "console-prompt";

    private final Prompter prompter;

    @Inject
    public ConsolePasswordPrompt(Prompter prompter) {
        this.prompter = prompter;
    }

    @Override
    public String description() {
        return "Secure console password prompt";
    }

    @Override
    public Optional<String> configTemplate() {
        return Optional.empty();
    }

    @Override
    public String handle(String config) throws SecDispatcherException {
        if (NAME.equals(config)) {
            try {
                return prompter.promptForPassword("Enter the master password: ");
            } catch (PrompterException e) {
                throw new SecDispatcherException("Could not collect the password", e);
            }
        }
        return null;
    }

    @Override
    public SecDispatcher.ValidationResponse validateConfiguration(String config) {
        if (NAME.equals(config)) {
            return new SecDispatcher.ValidationResponse(getClass().getSimpleName(), true, Map.of(), List.of());
        }
        return null;
    }
}
