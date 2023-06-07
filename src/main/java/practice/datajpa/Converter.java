package practice.datajpa;

import practice.datajpa.entity.composite.Priority;

import javax.persistence.AttributeConverter;

public class Converter {

    public static class PriorityConverter implements AttributeConverter<Priority, String> {

        @Override
        public String convertToDatabaseColumn(Priority attribute) {
            if (attribute == null) {
                return null;
            }
            return attribute.getPriority();
        }

        @Override
        public Priority convertToEntityAttribute(String dbData) {
            return Priority.getPriorityByCode(dbData);
        }
    }

}
