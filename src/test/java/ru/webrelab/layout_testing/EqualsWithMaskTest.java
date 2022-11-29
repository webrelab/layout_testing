package ru.webrelab.layout_testing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.webrelab.layout_testing.repository.AttributeRepository;
import ru.webrelab.layout_testing.screen_difference.DifferentElements;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EqualsWithMaskTest {
    private final List<String> expectedErrors = List.of("error1", "error2", "error3");
    private final TestRepo actual = new TestRepo(
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
            "Lorem",
            "",
            "Sed ut perspiciatis unde omnis iste natus error sit",
            "Sed",
            ""
    );

    @Test
    void stringWithoutMask() {
        final TestRepo expected = new TestRepo(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                "Lorem",
                "",
                "ed ut perspiciatis unde omnis iste natus error si",
                "Sed ut",
                "u"
        );
        assertEquals(expectedErrors, getReport(actual, expected));
    }

    @Test
    void onlyAsterisk() {
        final TestRepo expected = new TestRepo(
                "*",
                "*",
                "*",
                "",
                "",
                "Sed"
        );
        assertEquals(expectedErrors, getReport(actual, expected));
    }

    @Test
    void onlyTwoAsterisks() {
        final TestRepo expected = new TestRepo(
                "**",
                "***",
                "",
                "Sed",
                "Sed ut",
                "**"
        );
        assertEquals(expectedErrors, getReport(actual, expected));
    }

    @Test
    void onlyFirstAsterisk() {
        final TestRepo expected = new TestRepo(
                "* adipiscing elit",
                "*Lorem",
                "",
                "*Sed ut perspiciatis unde omnis iste natus error",
                "* Sed",
                "*Sed"
        );
        assertEquals(expectedErrors, getReport(actual, expected));
    }

    @Test
    void onlyLastAsterisk() {
        final TestRepo expected = new TestRepo(
                "Lorem ipsum dolor*",
                "Lorem*",
                "",
                "ut perspiciatis unde omnis iste natus error sit*",
                "Sed *",
                "Sed*"
        );
        assertEquals(expectedErrors, getReport(actual, expected));
    }

    @Test
    void doubleSideAsterisk() {
        final TestRepo expected = new TestRepo(
                "* sit amet, consec*",
                "*Lorem*",
                "",
                "*perspiciatis unde omnis iste natus error sit *",
                "* Sed*",
                "*Sed*"
        );
        assertEquals(expectedErrors, getReport(actual, expected));
    }

    @Test
    void asteriskAtLeftAndMiddle() {
        final TestRepo expected = new TestRepo(
                "*dolor sit amet, * adipiscing elit",
                "*Lo*m",
                "",
                "*omnis iste natus error sit*Sed ut perspiciatis unde",
                "*Se*ed",
                "*S*e"
        );
        assertEquals(expectedErrors, getReport(actual, expected));
    }

    @Test
    void asteriskAtMiddleAndRight() {
        final TestRepo expected = new TestRepo(
                "Lorem ipsum dolor * consectetur adipiscing *",
                "L*e*",
                "",
                "Sed ut perspicia *ti*",
                "Se*d *",
                "Sed"
        );
        assertEquals(expectedErrors, getReport(actual, expected));
    }

    @Test
    void doubleSideAndMiddleAsterisk() {
        final TestRepo expected = new TestRepo(
                "* dolor sit am*etur adipiscing elit*",
                "*Lor*em*",
                "",
                "*unde*perspiciatis omnis iste natus error sit*",
                "*S*ed *",
                "Sed"
        );
        assertEquals(expectedErrors, getReport(actual, expected));
    }

    @Test
    void onlyMiddleAsterisk() {
        final TestRepo expected = new TestRepo(
                "Lore*lit",
                "Lor*em",
                "",
                "e omnis iste natus error sit*Sed ut perspiciatis und",
                "S *ed",
                "Sed"
        );
        assertEquals(expectedErrors, getReport(actual, expected));
    }

    @Test
    void doubleMiddleAsterisk() {
        final TestRepo expected = new TestRepo(
                "Lo*r sit amet, * adipiscing elit",
                "Lo*r*em",
                "",
                "Sed ut *perspiciatis unde omni*s iste natus error",
                "d*e*S",
                "Sed"
        );
        assertEquals(expectedErrors, getReport(actual, expected));
    }

    @Test
    void doubleSideAndDoubleMiddleAsterisk() {
        final TestRepo expected = new TestRepo(
                "* ipsum * sit amet, conse*ctetur adipiscing*",
                "*Lo*r*m*",
                "",
                "* S*i*t*",
                "*S*ee*d*",
                "Sed"
        );
        assertEquals(expectedErrors, getReport(actual, expected));
    }

    private List<String> getReport(final TestRepo actual, final TestRepo expected) {
        return actual.compareWith(expected)
                .stream()
                .map(DifferentElements::getName)
                .collect(Collectors.toList());
    }


    @RequiredArgsConstructor
    @Getter
    static class TestRepo extends AttributeRepository {
        private final String success1;
        private final String success2;
        private final String success3;
        private final String error1;
        private final String error2;
        private final String error3;

        @Override
        public boolean check() {
            return true;
        }
    }
}
