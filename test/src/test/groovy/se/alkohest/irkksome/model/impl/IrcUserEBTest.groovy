package se.alkohest.irkksome.model.impl

import se.alkohest.irkksome.ColorProviderMockSpecification
import se.alkohest.irkksome.model.entity.IrcUser

public class IrcUserEBTest extends ColorProviderMockSpecification {
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