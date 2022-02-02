/*
 * Licensed to Elasticsearch B.V. under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch B.V. licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

//----------------------------------------------------
// THIS CODE IS GENERATED. MANUAL EDITS WILL BE LOST.
//----------------------------------------------------

package org.opensearch.clients.elasticsearch._types.aggregations;

import org.opensearch.clients.json.JsonpDeserializable;
import org.opensearch.clients.json.JsonpDeserializer;
import org.opensearch.clients.json.JsonpMapper;
import org.opensearch.clients.json.ObjectBuilderDeserializer;
import org.opensearch.clients.json.ObjectDeserializer;
import org.opensearch.clients.util.ObjectBuilder;
import jakarta.json.stream.JsonGenerator;
import java.util.function.Function;
import javax.annotation.Nullable;

// typedef: _types.aggregations.IpRangeBucket

/**
 *
 * @see <a href=
 *      "https://github.com/elastic/elasticsearch-specification/tree/98036c3/specification/_types/aggregations/Aggregate.ts#L526-L529">API
 *      specification</a>
 */
@JsonpDeserializable
public class IpRangeBucket extends MultiBucketBase {
	@Nullable
	private final String from;

	@Nullable
	private final String to;

	// ---------------------------------------------------------------------------------------------

	private IpRangeBucket(Builder builder) {
		super(builder);

		this.from = builder.from;
		this.to = builder.to;

	}

	public static IpRangeBucket of(Function<Builder, ObjectBuilder<IpRangeBucket>> fn) {
		return fn.apply(new Builder()).build();
	}

	/**
	 * API name: {@code from}
	 */
	@Nullable
	public final String from() {
		return this.from;
	}

	/**
	 * API name: {@code to}
	 */
	@Nullable
	public final String to() {
		return this.to;
	}

	protected void serializeInternal(JsonGenerator generator, JsonpMapper mapper) {

		super.serializeInternal(generator, mapper);
		if (this.from != null) {
			generator.writeKey("from");
			generator.write(this.from);

		}
		if (this.to != null) {
			generator.writeKey("to");
			generator.write(this.to);

		}

	}

	// ---------------------------------------------------------------------------------------------

	/**
	 * Builder for {@link IpRangeBucket}.
	 */

	public static class Builder extends MultiBucketBase.AbstractBuilder<Builder>
			implements
				ObjectBuilder<IpRangeBucket> {
		@Nullable
		private String from;

		@Nullable
		private String to;

		/**
		 * API name: {@code from}
		 */
		public final Builder from(@Nullable String value) {
			this.from = value;
			return this;
		}

		/**
		 * API name: {@code to}
		 */
		public final Builder to(@Nullable String value) {
			this.to = value;
			return this;
		}

		@Override
		protected Builder self() {
			return this;
		}

		/**
		 * Builds a {@link IpRangeBucket}.
		 *
		 * @throws NullPointerException
		 *             if some of the required fields are null.
		 */
		public IpRangeBucket build() {
			_checkSingleUse();

			return new IpRangeBucket(this);
		}
	}

	// ---------------------------------------------------------------------------------------------

	/**
	 * Json deserializer for {@link IpRangeBucket}
	 */
	public static final JsonpDeserializer<IpRangeBucket> _DESERIALIZER = ObjectBuilderDeserializer.lazy(Builder::new,
			IpRangeBucket::setupIpRangeBucketDeserializer);

	protected static void setupIpRangeBucketDeserializer(ObjectDeserializer<IpRangeBucket.Builder> op) {
		MultiBucketBase.setupMultiBucketBaseDeserializer(op);
		op.add(Builder::from, JsonpDeserializer.stringDeserializer(), "from");
		op.add(Builder::to, JsonpDeserializer.stringDeserializer(), "to");

	}

}
