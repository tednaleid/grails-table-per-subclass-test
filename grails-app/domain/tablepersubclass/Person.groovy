package tablepersubclass

class Person {
    String firstName
    String lastName

    static hasMany = [houses: House]

    static belongsTo = [organization: Organization]

    static mapping = {
        tablePerHierarchy false
        organization fetch: "JOIN"
    }

    def beforeValidate() {
        return foo()
    }

    def foo() {
        lastName = organization.name.reverse()
    }

    static constraints = {
    }
}
