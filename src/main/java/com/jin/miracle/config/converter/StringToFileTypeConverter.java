package com.jin.miracle.config.converter;

import com.jin.miracle.file.FileType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToFileTypeConverter implements Converter<String, FileType> {
    @Override
    public FileType convert(String source) {
        return FileType.valueOf(source.toUpperCase() + "_DIR");
    }
}