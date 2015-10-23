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

package org.apache.lucene.search;

import org.apache.lucene.index.IndexReader;
import org.elasticsearch.common.geo.GeoUtils;

import java.io.IOException;

/**
 *
 */
public final class XGeoPointDistanceRangeQuery extends XGeoPointDistanceQuery {
  protected final double minRadiusMeters;

  public XGeoPointDistanceRangeQuery(final String field, final double centerLon, final double centerLat,
                                     final double minRadius, final double maxRadius) {
    super(field, centerLon, centerLat, maxRadius);
    this.minRadiusMeters = minRadius;
  }

  @Override
  public Query rewrite(IndexReader reader) throws IOException {
    if (getBoost() != 1f) {
      super.rewrite(reader);
    }
    Query q = super.rewrite(reader);
    if (minRadiusMeters == 0.0) {
      return q;
    }

    // add an exclusion query
    BooleanQuery.Builder bqb = new BooleanQuery.Builder();

    // create a new exclusion query
    XGeoPointDistanceQuery exclude = new XGeoPointDistanceQuery(field, centerLon, centerLat, minRadiusMeters);
    bqb.add(new BooleanClause(q, BooleanClause.Occur.MUST));
    bqb.add(new BooleanClause(exclude, BooleanClause.Occur.MUST_NOT));

    return bqb.build();
  }

  public double getMinRadiusMeters() {
    return this.minRadiusMeters;
  }

  public double getMaxRadiusMeters() {
    return this.radius;
  }
}
