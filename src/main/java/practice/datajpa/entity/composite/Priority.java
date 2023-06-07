package practice.datajpa.entity.composite;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Priority {
    LOW("L", 2),
    MEDIUM("M", 1),
    HIGH("H", 0),
    ETC("E", 9);

    private String priority;
    private int number;

    Priority(String priority, int number) {
        this.priority = priority;
        this.number = number;
    }

    public static Priority getPriorityByCode(String code) {
        return Stream.of(Priority.values())
                     .filter(p -> p.getPriority().equals(code))
                     .findFirst()
                     .orElseGet(Priority::etc);
    }

    static Priority etc() {
        return Priority.ETC;
    }
}
