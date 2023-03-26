package ru.javaops.balykov.restaurantvoting.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import ru.javaops.balykov.restaurantvoting.model.BaseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtil {
    public static void assertRecursiveEquals(BaseEntity actual, BaseEntity expected,
                                             RecursiveComparisonConfiguration configuration) {
        assertThat(actual).usingRecursiveComparison(configuration).isEqualTo(expected);
    }

    public static void assertRecursiveEquals(List<? extends BaseEntity> actual, List<? extends BaseEntity> expected,
                                             RecursiveComparisonConfiguration configuration) {
        assertThat(actual).usingRecursiveComparison(configuration).isEqualTo(expected);
    }
}
