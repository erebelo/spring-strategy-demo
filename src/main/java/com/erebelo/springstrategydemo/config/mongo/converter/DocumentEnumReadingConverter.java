package com.erebelo.springstrategydemo.config.mongo.converter;

import java.util.Arrays;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
@RequiredArgsConstructor
public class DocumentEnumReadingConverter implements ConverterFactory<@NonNull Document, @NonNull DocumentEnum> {

    @Override
    public <T extends DocumentEnum> @NonNull Converter<@NonNull Document, ? extends T> getConverter(
            Class<T> targetType) {
        if (!targetType.isEnum()) {
            throw new IllegalStateException(
                    "The targetType [%s] must be an enum.".formatted(targetType.getSimpleName()));
        }

        return source -> Arrays.stream(targetType.getEnumConstants()).filter(enumValue -> matches(source, enumValue))
                .findFirst().orElseThrow(
                        () -> new IllegalStateException("The document [%s] doesn't match any instance of the enum [%s]."
                                .formatted(source, targetType.getSimpleName())));
    }

    private boolean matches(Document source, DocumentEnum enumValue) {
        Map<String, Object> attributes = DocumentEnumReflector.extractAttributes(enumValue);
        return source.equals(new Document(attributes));
    }
}
