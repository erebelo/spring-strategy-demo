package com.erebelo.springstrategydemo.config.mongo.converter;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
@RequiredArgsConstructor
public class DocumentEnumWritingConverter implements Converter<@NonNull DocumentEnum, Document> {

    @Override
    public Document convert(DocumentEnum source) {
        return new Document(DocumentEnumReflector.extractAttributes(source));
    }
}
