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
import java.util.function.Function;
import javax.annotation.Nullable;

// typedef: nodes.info.NodeInfoSettingsTransport

/**
 *
 * @see <a href=
 *      "https://github.com/elastic/elasticsearch-specification/tree/98036c3/specification/nodes/info/types.ts#L190-L194">API
 *      specification</a>
 */
@JsonpDeserializable
public class NodeInfoSettingsTransport implements JsonpSerializable {
	private final NodeInfoSettingsTransportType type;

	@Nullable
	private final String typeDefault;

	@Nullable
	private final NodeInfoSettingsTransportFeatures features;

	// ---------------------------------------------------------------------------------------------

	private NodeInfoSettingsTransport(Builder builder) {

		this.type = ApiTypeHelper.requireNonNull(builder.type, this, "type");
		this.typeDefault = builder.typeDefault;
		this.features = builder.features;

	}

	public static NodeInfoSettingsTransport of(Function<Builder, ObjectBuilder<NodeInfoSettingsTransport>> fn) {
		return fn.apply(new Builder()).build();
	}

	/**
	 * Required - API name: {@code type}
	 */
	public final NodeInfoSettingsTransportType type() {
		return this.type;
	}

	/**
	 * API name: {@code type.default}
	 */
	@Nullable
	public final String typeDefault() {
		return this.typeDefault;
	}

	/**
	 * API name: {@code features}
	 */
	@Nullable
	public final NodeInfoSettingsTransportFeatures features() {
		return this.features;
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

		generator.writeKey("type");
		this.type.serialize(generator, mapper);

		if (this.typeDefault != null) {
			generator.writeKey("type.default");
			generator.write(this.typeDefault);

		}
		if (this.features != null) {
			generator.writeKey("features");
			this.features.serialize(generator, mapper);

		}

	}

	// ---------------------------------------------------------------------------------------------

	/**
	 * Builder for {@link NodeInfoSettingsTransport}.
	 */

	public static class Builder extends ObjectBuilderBase implements ObjectBuilder<NodeInfoSettingsTransport> {
		private NodeInfoSettingsTransportType type;

		@Nullable
		private String typeDefault;

		@Nullable
		private NodeInfoSettingsTransportFeatures features;

		/**
		 * Required - API name: {@code type}
		 */
		public final Builder type(NodeInfoSettingsTransportType value) {
			this.type = value;
			return this;
		}

		/**
		 * Required - API name: {@code type}
		 */
		public final Builder type(
				Function<NodeInfoSettingsTransportType.Builder, ObjectBuilder<NodeInfoSettingsTransportType>> fn) {
			return this.type(fn.apply(new NodeInfoSettingsTransportType.Builder()).build());
		}

		/**
		 * API name: {@code type.default}
		 */
		public final Builder typeDefault(@Nullable String value) {
			this.typeDefault = value;
			return this;
		}

		/**
		 * API name: {@code features}
		 */
		public final Builder features(@Nullable NodeInfoSettingsTransportFeatures value) {
			this.features = value;
			return this;
		}

		/**
		 * API name: {@code features}
		 */
		public final Builder features(
				Function<NodeInfoSettingsTransportFeatures.Builder, ObjectBuilder<NodeInfoSettingsTransportFeatures>> fn) {
			return this.features(fn.apply(new NodeInfoSettingsTransportFeatures.Builder()).build());
		}

		/**
		 * Builds a {@link NodeInfoSettingsTransport}.
		 *
		 * @throws NullPointerException
		 *             if some of the required fields are null.
		 */
		public NodeInfoSettingsTransport build() {
			_checkSingleUse();

			return new NodeInfoSettingsTransport(this);
		}
	}

	// ---------------------------------------------------------------------------------------------

	/**
	 * Json deserializer for {@link NodeInfoSettingsTransport}
	 */
	public static final JsonpDeserializer<NodeInfoSettingsTransport> _DESERIALIZER = ObjectBuilderDeserializer
			.lazy(Builder::new, NodeInfoSettingsTransport::setupNodeInfoSettingsTransportDeserializer);

	protected static void setupNodeInfoSettingsTransportDeserializer(
			ObjectDeserializer<NodeInfoSettingsTransport.Builder> op) {

		op.add(Builder::type, NodeInfoSettingsTransportType._DESERIALIZER, "type");
		op.add(Builder::typeDefault, JsonpDeserializer.stringDeserializer(), "type.default");
		op.add(Builder::features, NodeInfoSettingsTransportFeatures._DESERIALIZER, "features");

	}

}
