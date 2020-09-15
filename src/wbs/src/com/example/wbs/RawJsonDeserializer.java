package com.example.wbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.jena.riot.thrift.wire.RDF_ANY;
import org.springframework.lang.NonNull;

import java.beans.Transient;
import java.io.IOException;
import java.util.Objects;

/* @EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawJsonDeserializer extends JsonDeserializer<String> {

    @NonNull
    @JsonProperty("domainModelBody")
    @JsonRawValue
    @JsonDeserialize(using = RawJsonDeserializer.class)
    private String domainModelBody;

//    @Transient
    private Class<?> domainModelClass;

    @JsonProperty("domainModelClass")
    private String domainModelClassName;

    @JsonCreator
    public RawJsonDeserializer(Class<?> domainModelClass,
                               @JsonProperty("domainModelBody") @NonNull String domainModelBody) {
        this.domainModelClass = domainModelClass;
        this.domainModelBody = domainModelBody;
        this.domainModelClassName = domainModelClass.getName();
    }

    @NonNull
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode jsonNode = objectMapper.readTree(jsonParser);
        return objectMapper.writeValueAsString(jsonNode);
    }

    @NonNull
    public <T extends Object> T toDomainModel(@NonNull ObjectMapper objectMapper, @NonNull Class<T> domainModelClass) {
        Objects.requireNonNull(objectMapper, "Object mapper must not be null. ");
        Objects.requireNonNull(domainModelClass, "Domain model class must not be null. ");
        try {
            String domainModelBody = domainModelClass.toString();
            return objectMapper.readValue(domainModelBody, domainModelClass);
        }
        catch (IOException ex) {
            throw new IllegalStateException("Could not deserialize domain model from JSON", ex);
        }
    }

    @NonNull
    public String toJsonString() {
        return this.domainModelBody;
    }

    @NonNull
    public JsonNode toJsonNode(@NonNull ObjectMapper objectMapper) {
    //    Objects.requireNonNull(objectMapper, "Object mapper must not be null. ");
        try {
            return objectMapper.readTree(this.domainModelBody);
        }
        catch (IOException ex) {
            throw new IllegalStateException("Could not deserialize domain model from JSON", ex);
        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private Class<?> lookUpDomainModelClass() throws ClassNotFoundException {
        try {
            return (Class<?>) Class.forName(domainModelClassName);
        }
        catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Could not load domain model class", ex);
        }
    }
} */
