package se.alkohest.irkksome

import se.alkohest.irkksome.util.ColorProvider
import spock.lang.Specification

public class ColorProviderMockSpecification extends Specification {
    def setup() {
        ColorProvider mock = Mock(ColorProvider)
        ColorProvider.INSTANCE = mock
        mock.getColor() >> 0
    }

    def happyTest() {
        expect:
        true
    }
}