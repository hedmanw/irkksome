package se.alkohest.irkksome.db

import se.alkohest.irkksome.orm.ORMException
import spock.lang.Specification

public class SQLitePersistenceContextTest extends Specification {
    def persistenceContext = new SQLitePersistenceContext()
    def databaseHelper = Mock(SQLiteAdapter)

    def setup() {
        persistenceContext.database = databaseHelper;
    }

    def "Make valid CREATE"() {
        setup:
        databaseHelper.insert("t_test", null) >> 1337

        when:
        long pk = persistenceContext.create("t_test", null)

        then:
        pk == 1337
    }

    def "Make invalid CREATE"() {
        setup:
        databaseHelper.insert("t_nope", null) >> -1

        when:
        def pk = persistenceContext.create("t_nope", null)

        then:
        thrown(ORMException)
    }
}