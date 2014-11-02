package se.alkohest.irkksome.util

import spock.lang.Specification

/**
 * Created by wilhelm 2014-11-02.
 */
class DateFormatUtilTest extends Specification {
    def "GetTimestamp"() {
        when:
        Calendar calendar = GregorianCalendar.getInstance()
        calendar.set(50, 1, 1, 13, 37)
        then:
        DateFormatUtil.getTimestamp(calendar.getTime()) == "13:37"
    }

    def "GetTimePassed"() {
    }
}
