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

package org.opensearch.clients.elasticsearch.nodes.info;

import org.opensearch.clients.json.JsonpDeserializable;
import org.opensearch.clients.json.JsonpDeserializer;
import org.opensearch.clients.json.JsonpMapper;
import org.opensearch.clients.json.JsonpSerializable;
import org.opensearch.clients.json.ObjectBuilderDeserializer;
import org.opensearch.clients.json.ObjectDeserializer;
import org.opensearch.clients.util.ApiTypeHelper;
import org.opensearch.clients.util.ObjectBuilder;
import org.opensearch.clients.util.ObjectBuilderBase;
import jakarta.json.stream.JsonGenerator;
import java.util.List;
import java.util.function.Function;

// typedef: nodes.info.NodeInfoAggregation

/**
 *
 * @see <a href=
 *      "https://github.com/elastic/elasticsearch-specification/tree/98036c3/specification/nodes/info/types.ts#L217-L219">API
 *      specification</a>
 */
@JsonpDeserializable
public class NodeInfoAggregation implements JsonpSerializable {
	private final List<String> types;

	// ---------------------------------------------------------------------------------------------

	private NodeInfoAggregation(Builder builder) {

		this.types = ApiTypeHelper.unmodifiableRequired(builder.types, this, "types");

	}

	public static NodeInfoAggregation of(Function<Builder, ObjectBuilder<NodeInfoAggregation>> fn) {
		return fn.apply(new Builder()).build();
	}

	/**
	 * Required - API name: {@code types}
	 */
	public final List<String> types() {
		return this.types;
	}

	/**
	 * Serialize this object to JSON.
	 */
	public void serialize(JsonGenerator generator, JsonpMapper mapper) {
		generator.writeStartObject();
		serializeInternal(generator, mapper);
		generator.writeEnd();
	}

	protected void serializeInternal(JsonGenerator generator, JsonpMapper mapper) {

		if (ApiTypeHelper.isDefined(this.types)) {
			generator.writeKey("types");
			generator.writeStartArray();
			for (String item0 : this.types) {
				generator.write(item0);

			}
			generator.writeEnd();

		}

	}

	// ---------------------------------------------------------------------------------------------

	/**
	 * Builder for {@link NodeInfoAggregation}.
	 */

	public static class Builder extends ObjectBuilderBase implements ObjectBuilder<NodeInfoAggregation> {
		private List<String> types;

		/**
		 * Required - API name: {@code types}
		 * <p>
		 * Adds all elements of <code>list</code> to <code>types</code>.
		 */
		public final Builder types(List<String> list) {
			this.types = _listAddAll(this.types, list);
			return this;
		}

		/**
		 * Required - API name: {@code types}
		 * <p>
		 * Adds one or more values to <code>types</code>.
		 */
		public final Builder types(String value, String... values) {
			this.types = _listAdd(this.types, value, values);
			return this;
		}

		/**
		 * Builds a {@link NodeInfoAggregation}.
		 *
		 * @throws NullPointerException
		 *             if some of the required fields are null.
		 */
		public NodeInfoAggregation build() {
			_checkSingleUse();

			return new NodeInfoAggregation(this);
		}
	}

	// ---------------------------------------------------------------------------------------------

	/**
	 * Json deserializer for {@link NodeInfoAggregation}
	 */
	public static final JsonpDeserializer<NodeInfoAggregation> _DESERIALIZER = ObjectBuilderDeserializer
			.lazy(Builder::new, NodeInfoAggregation::setupNodeInfoAggregationDeserializer);

	protected static void setupNodeInfoAggregationDeserializer(ObjectDeserializer<NodeInfoAggregation.Builder> op) {

		op.add(Builder::types, JsonpDeserializer.arrayDeserializer(JsonpDeserializer.stringDeserializer()), "types");

	}

}
