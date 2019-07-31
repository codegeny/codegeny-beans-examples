/*-
 * #%L
 * codegeny-beans-examples
 * %%
 * Copyright (C) 2016 - 2019 Codegeny
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
package org.codegeny.beans.example;

import org.codegeny.beans.diff.Diff;
import org.codegeny.beans.model.visitor.diff.ComputeDiffModelVisitor;

import javax.enterprise.inject.Model;
import java.util.function.Predicate;

@Model
public class PersonDiffBean {

    private final Diff<Person> diff;

    public PersonDiffBean() {

        // create a default person
        Person left = Person.createDefaultPerson();

        // create a default person and modify it
        Person right = Person.createDefaultPerson()
                .setFirstName("Jack")
                .addMiddleName("Benjamin")
                .removeMiddleName(Predicate.isEqual("Patrick"))
                .addFormerAddress(new Address().setCountry("Russia").setStreet("Red Square").setZipCode("999"));

        right.getCurrentAddress().setStreet("Sunset Bld");
        right.getFormerAddresses().stream().filter(a -> "Belgium".equals(a.getCountry())).forEach(a -> a.setCountry(null));
        right.getFormerAddresses().stream().filter(a -> "France".equals(a.getCountry())).forEach(a -> a.setZipCode("1001"));
        right.setBirthDate(right.getBirthDate().minusYears(1));

        // compare those 2 persons
        diff = Person.MODEL.accept(new ComputeDiffModelVisitor<>(left, right, 0.5));
    }

    public Diff<Person> getDiff() {
        return diff;
    }
}
