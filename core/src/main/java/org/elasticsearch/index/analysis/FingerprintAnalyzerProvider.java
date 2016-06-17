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

package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;


/**
 * Builds an OpenRefine Fingerprint analyzer.  Uses the default settings from the various components
 * (Standard Tokenizer and lowercase + stop + fingerprint + ascii-folding filters)
 */
public class FingerprintAnalyzerProvider extends AbstractIndexAnalyzerProvider<Analyzer> {

    public static ParseField MAX_OUTPUT_SIZE = FingerprintTokenFilterFactory.MAX_OUTPUT_SIZE;

    public static int DEFAULT_MAX_OUTPUT_SIZE = FingerprintTokenFilterFactory.DEFAULT_MAX_OUTPUT_SIZE;
    public static CharArraySet DEFAULT_STOP_WORDS = CharArraySet.EMPTY_SET;

    private final FingerprintAnalyzer analyzer;

    public FingerprintAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);

        char separator = FingerprintTokenFilterFactory.parseSeparator(settings);
        int maxOutputSize = settings.getAsInt(MAX_OUTPUT_SIZE.getPreferredName(),DEFAULT_MAX_OUTPUT_SIZE);
        CharArraySet stopWords = Analysis.parseStopWords(env, settings, DEFAULT_STOP_WORDS);

        this.analyzer = new FingerprintAnalyzer(stopWords, separator, maxOutputSize);
    }

    @Override
    public FingerprintAnalyzer get() {
        return analyzer;
    }
}
