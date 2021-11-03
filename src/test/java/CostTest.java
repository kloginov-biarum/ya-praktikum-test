import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CostTest {
    Cost cost = new Cost();


    /**
     * С целью снижения уровня дублирования кода реализуем проверки позитивных кейсов
     * корректных входных значений с помощью параметризированного теста и провайдера данных
     * @param expected
     * @param distance
     * @param big
     * @param fragile
     * @param load
     */
    @ParameterizedTest
    @MethodSource("dataProvider")
    public void costCalcPositiveTest(double expected, double distance, boolean big, boolean fragile, String load) {
        assertEquals(expected, cost.costCalc(distance, big,fragile, load));
    }

    static Stream <Arguments> dataProvider(){
        return Stream.of(
                arguments(1440, 45, true, false, "очень высокая"),
                arguments(700, 15, false, false, "обычная"),
                arguments(1280, 15, true, false, "очень высокая"),
                arguments(1400,15, false, true, "высокая"),
                arguments(960, 15, true, false, "повышенная"),
                arguments(1200, 8, true, true, "повышенная"),
                arguments(600, 8, false, false, "2"),
                arguments(1600, 8, true, true, "очень высокая"),
                arguments(840, 8, false, false, "высокая"),
                arguments(1190, 1, false, true, "высокая"),
                arguments(1040, 1, true, false, "очень высокая"),
                arguments(780, 1, true, false, "повышенная"),
                arguments(850, 1, false, true, "обычная"),
                arguments(1140, 0, true, true, "повышенная"),
                arguments(1190, 2, false, true, "высокая"),
                arguments(780, 2, true, false, "повышенная"),
                arguments(850, 2, false, true, "обычная"),
                arguments(1040, 2, true, false, "очень высокая"),
                arguments(1600, 10, true, true, "очень высокая"),
                arguments(840, 10, false, false, "высокая"),
                arguments(1200, 10, true, true, "повышенная"),
                arguments(600, 10, false, false, "обычная"),
                arguments(1400, 30, false, true, "высокая"),
                arguments(960, 30, true, false, "повышенная"),
                arguments(1000, 30, false, true, "обычная"),
                arguments(1280, 30, true, false, "очень высокая")
        );
    }

    /**
     * Выполним набор проверок негативных кейсов
     * @param expected
     * @param distance
     * @param big
     * @param fragile
     * @param load
     */
    @ParameterizedTest
    @MethodSource("dataProviderNegative")
    public void costCalcNegativeTest(double expected, double distance, boolean big, boolean fragile, String load) {
        assertNotEquals(expected, cost.costCalc(distance, big,fragile, load));
    }

    static Stream <Arguments> dataProviderNegative() {
        return Stream.of(
                arguments(1500, 45, true, false, "очень высокая"),
                arguments(700, 13, false, true, "повышенная"),
                arguments(500, 7, true, true, "высокая"),
                arguments(1000, 1.5, false, false, "" )

        );
    }



    /**
     * Реализуем проверки исключений и текстов сообщений исключений
     * @param exceptionMessage
     * @param distance
     * @param big
     * @param fragile
     * @param load
     */
    @ParameterizedTest
    @MethodSource("dataProviderException")
    public void costCalcExceptionTest(String exceptionMessage,double distance, boolean big, boolean fragile, String load){
        Throwable exceptionThatWasThrown = assertThrows(IllegalArgumentException.class, () -> {
            cost.costCalc(distance, big,fragile, load);
        });
        assertTrue(exceptionThatWasThrown.getMessage().contains(exceptionMessage));
    }

    static Stream <Arguments>  dataProviderException(){
        return Stream.of(
                arguments("Расстояние не может быть меньше нуля, физика против!" ,-1,true, false, ""),
                arguments("Хрупкие грузы нельзя возить далее 30 км", 50, false, true, "высокая")
        );
    }

    /**
     * Убедимся, что NullPointer exception тоже отрабатывет
     */
    @Test
    public void costCalcNullPointerExceptionTest(){
        Throwable exceptionThatWasThrown = assertThrows(NullPointerException.class, () -> {
            cost.costCalc(10, true,false, null);
        });
        assertTrue(exceptionThatWasThrown.getClass().equals(NullPointerException.class));
    }

}