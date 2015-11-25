/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.ingest.processor.remove;

import org.elasticsearch.ingest.IngestDocument;
import org.elasticsearch.ingest.RandomDocumentPicks;
import org.elasticsearch.ingest.processor.Processor;
import org.elasticsearch.test.ESTestCase;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class RemoveProcessorTests extends ESTestCase {

    public void testRemoveFields() throws IOException {
        IngestDocument ingestDocument = RandomDocumentPicks.randomIngestDocument(random());
        int numFields = randomIntBetween(1, 5);
        Set<String> fields = new HashSet<>();
        for (int i = 0; i < numFields; i++) {
            fields.add(RandomDocumentPicks.randomExistingFieldName(random(), ingestDocument));
        }
        Processor processor = new RemoveProcessor(fields);
        processor.execute(ingestDocument);
        for (String field : fields) {
            assertThat(ingestDocument.getPropertyValue(field, Object.class), nullValue());
            assertThat(ingestDocument.hasPropertyValue(field), equalTo(false));
        }
    }

    public void testRemoveNonExistingField() throws IOException {
        IngestDocument ingestDocument = RandomDocumentPicks.randomIngestDocument(random(), new HashMap<>());
        Processor processor = new RemoveProcessor(Collections.singletonList(RandomDocumentPicks.randomFieldName(random())));
        processor.execute(ingestDocument);
        assertThat(ingestDocument.getSource().size(), equalTo(0));
    }
}