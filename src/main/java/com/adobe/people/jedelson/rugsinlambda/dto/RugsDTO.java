/*
 * #%L
 * %%
 * Copyright (C) 2017 Adobe
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.adobe.people.jedelson.rugsinlambda.dto;

import com.atomist.rug.runtime.ParameterizedRug;

import java.util.ArrayList;
import java.util.List;

public class RugsDTO {

    private final List<RugDTO> editors = new ArrayList<>();
    private final List<RugDTO> generators = new ArrayList<>();

    public List<RugDTO> getEditors() {
        return editors;
    }

    public List<RugDTO> getGenerators() {
        return generators;
    }

    public void addGenerator(ParameterizedRug g) {
        generators.add(new RugDTO(g));
    }

    public void addEditor(ParameterizedRug e) {
        editors.add(new RugDTO(e));
    }
}
