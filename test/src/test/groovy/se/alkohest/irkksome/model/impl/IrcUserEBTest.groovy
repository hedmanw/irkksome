package se.alkohest.irkksome.model.impl

import android.graphics.Color
import se.alkohest.irkksome.model.entity.IrcUser
import spock.lang.Specification

public class IrcUserEBTest extends Specification {

    def setup() {
        def color = Mock(Color)
        color.HSVToColor(_) >> null
    }

    def "getSetName"() {
        when:
        IrcUser user = new IrcUserEB();
        user.setName("Korvryttarn")

        then:
        user.getName() == "Korvryttarn"

        when:
        user.setName("Inte Korvryttarn")

        then:
        user.getName() != "Korvryttarn"
        user.getName() == "Inte Korvryttarn"
    }
}