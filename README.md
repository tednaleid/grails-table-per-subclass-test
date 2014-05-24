Simple grails 2.3.8 test of the `table-per-subclass` mapping and how it performs.  Hibernate SQL tracing is on.

The `grails-app/domain/tablepersubclass` directory has all of the domain classes.  In short there's a `Person` concrete superclass, and `Programmer`, `Teacher` and `Student` subclasses that exercise various relationships. 

## Running

It's currently configured to run against mysql.  You can set up the tables and the expected user by running the `bin/dropCreate.sh` script.

Both the `build-test-data` and `grails-console` plugins are installed.  If you start up the app and go to [http://localhost:8080/tablePerSubclassTest/console/index](http://localhost:8080/tablePerSubclassTest/console/index), you can try it out.

## Testing/Using

Here's a sample script to try:

```
import tablepersubclass.*
  

println "--  find/insert Person"
Person person = Person.buildLazy(firstName: "Alpha")

println "-- find/insert Programmer"
Programmer programmer = Programmer.buildLazy(firstName: "Bravo", yearlySalary: 80000.00)

println "-- find/insert Teacher"
Teacher teacher = Teacher.buildLazy(firstName: "Charlie", yearlySalary: 50000.00)

println "-- find/insert Student"
Student student = Student.buildLazy(firstName: "Delta", teacher: teacher, gradeLevel: 6)

println "-- Person.list() - retrieves hydrated subclasses not Person objects"
List<Person> people = Person.list()

people.each { println it.class }

println "-- Person.count() - does joins when it shouldn't need to"
Person.count()

println "-- Person.findByFirstName(\"Delta\")"
assert Person.findByFirstName("Delta").class == Student

println "-- Person.executeQuery(\"select id from Person p where p.firstName = 'Bravo'\")"
println "-- `executeQuery` smart enough to not join in when not selecting fields from subclasses"
Person.executeQuery("select id from Person p where p.firstName = 'Bravo'")


println "-- select superclass with where clause using property in 2 of 3 subclasses"
println "-- only smart enough to pick one of the tables with that property (here it picks the \"teacher\" table in the `where` clause)"
println "-- Person.executeQuery(\"from Person p where p.yearlySalary > :yearlySalary\", [yearlySalary: 40000.00])"

Person.executeQuery("from Person p where p.yearlySalary > :yearlySalary", [yearlySalary: 40000.00])

println "-- Programmer.list()"
println "-- using a subclass will use the correct tables and not leak into the other subclasses"
Programmer.list()
```

There is also a simple Spock integration test `Specification` that could be enhanced with further tests.


This is the SQL output of that script showing how the app will run queries against the superclass and try to `outer join` all subclasses.  


``` sql
-- find/insert Person
select this_.id as id2_0_, this_.version as version2_0_, this_.first_name as first3_2_0_, this_.last_name as last4_2_0_, this_1_.grade_level as grade2_3_0_, this_1_.teacher_id as teacher3_3_0_, this_2_.grade_level as grade2_5_0_, this_3_.yearly_salary as yearly2_6_0_, case when this_1_.id is not null then 1 when this_2_.id is not null then 2 when this_3_.id is not null then 3 when this_.id is not null then 0 end as clazz_0_ from person this_ left outer join student this_1_ on this_.id=this_1_.id left outer join teacher this_2_ on this_.id=this_2_.id left outer join programmer this_3_ on this_.id=this_3_.id where (this_.first_name=?) limit ?

insert into person (version, first_name, last_name) values (?, ?, ?)

-- find/insert Programmer
select this_.id as id2_0_, this_1_.version as version2_0_, this_1_.first_name as first3_2_0_, this_1_.last_name as last4_2_0_, this_.yearly_salary as yearly2_6_0_ from programmer this_ inner join person this_1_ on this_.id=this_1_.id where (this_1_.first_name=? and this_.yearly_salary=?) limit ?

insert into person (version, first_name, last_name) values (?, ?, ?)

insert into programmer (yearly_salary, id) values (?, ?)

-- find/insert Teacher
select this_.id as id2_0_, this_1_.version as version2_0_, this_1_.first_name as first3_2_0_, this_1_.last_name as last4_2_0_, this_.grade_level as grade2_5_0_ from teacher this_ inner join person this_1_ on this_.id=this_1_.id where (this_1_.first_name=? and this_.grade_level=?) limit ?

insert into person (version, first_name, last_name) values (?, ?, ?)

insert into teacher (grade_level, id) values (?, ?)

insert into white_board (version, teacher_id) values (?, ?)

-- find/insert Student
select this_.id as id2_0_, this_1_.version as version2_0_, this_1_.first_name as first3_2_0_, this_1_.last_name as last4_2_0_, this_.grade_level as grade2_3_0_, this_.teacher_id as teacher3_3_0_ from student this_ inner join person this_1_ on this_.id=this_1_.id where (this_1_.first_name=? and this_.teacher_id=? and this_.grade_level=?) limit ?

insert into person (version, first_name, last_name) values (?, ?, ?)

insert into student (grade_level, teacher_id, id) values (?, ?, ?)

-- Person.list() - retrieves hydrated subclasses not Person objects
select this_.id as id2_0_, this_.version as version2_0_, this_.first_name as first3_2_0_, this_.last_name as last4_2_0_, this_1_.grade_level as grade2_3_0_, this_1_.teacher_id as teacher3_3_0_, this_2_.grade_level as grade2_5_0_, this_3_.yearly_salary as yearly2_6_0_, case when this_1_.id is not null then 1 when this_2_.id is not null then 2 when this_3_.id is not null then 3 when this_.id is not null then 0 end as clazz_0_ from person this_ left outer join student this_1_ on this_.id=this_1_.id left outer join teacher this_2_ on this_.id=this_2_.id left outer join programmer this_3_ on this_.id=this_3_.id

class tablepersubclass.Student
class tablepersubclass.Teacher
class tablepersubclass.Programmer
class tablepersubclass.Person

-- Person.count() - does joins when it shouldn't need to
select count(*) as y0_ from person this_ left outer join student this_1_ on this_.id=this_1_.id left outer join teacher this_2_ on this_.id=this_2_.id left outer join programmer this_3_ on this_.id=this_3_.id

-- Person.findByFirstName("Delta")
select this_.id as id2_0_, this_.version as version2_0_, this_.first_name as first3_2_0_, this_.last_name as last4_2_0_, this_1_.grade_level as grade2_3_0_, this_1_.teacher_id as teacher3_3_0_, this_2_.grade_level as grade2_5_0_, this_3_.yearly_salary as yearly2_6_0_, case when this_1_.id is not null then 1 when this_2_.id is not null then 2 when this_3_.id is not null then 3 when this_.id is not null then 0 end as clazz_0_ from person this_ left outer join student this_1_ on this_.id=this_1_.id left outer join teacher this_2_ on this_.id=this_2_.id left outer join programmer this_3_ on this_.id=this_3_.id where this_.first_name=? limit ?

-- Person.executeQuery("select id from Person p where p.firstName = 'Bravo'")
-- `executeQuery` smart enough to not join in when not selecting fields from subclasses
select person0_.id as col_0_0_ from person person0_ where person0_.first_name='Bravo'


-- select superclass with where clause using property in 2 of 3 subclasses
-- only smart enough to pick one of the tables with that property (here it picks the "teacher" table in the `where` clause)
-- Person.executeQuery("from Person p where p.yearlySalary > :yearlySalary", [yearlySalary: 40000.00])
select person0_.id as id2_, person0_.version as version2_, person0_.first_name as first3_2_, person0_.last_name as last4_2_, person0_1_.grade_level as grade2_3_, person0_1_.teacher_id as teacher3_3_, person0_2_.yearly_salary as yearly2_5_, person0_3_.yearly_salary as yearly2_6_, case when person0_1_.id is not null then 1 when person0_2_.id is not null then 2 when person0_3_.id is not null then 3 when person0_.id is not null then 0 end as clazz_
from person person0_
left outer join student person0_1_    on person0_.id = person0_1_.id
left outer join teacher person0_2_    on person0_.id = person0_2_.id
left outer join programmer person0_3_ on person0_.id = person0_3_.id
where person0_2_.yearly_salary > ?

-- Programmer.list()
-- using a subclass will use the correct tables and not leak into the other subclasses
select this_.id as id1_0_, this_1_.version as version1_0_, this_1_.first_name as first3_1_0_, this_1_.last_name as last4_1_0_, this_.yearly_salary as yearly2_4_0_
from programmer this_
inner join person this_1_ on this_.id = this_1_.id
```


The `table-per-subclass` mapping does not allow for the use of a discriminator field, unlike the `table-per-heirarchy` setting.  

As you can see above, queries on the superclass return fully hydrated subclassed objects.  Though it is smart enough to limit the query to the superclass if you only ask for values from the superclass.
 
This is why the grails documentation says that "can result in poor query performance due to the use of outer join queries".

Also, in Grails 2.3.X, database-level foreign key constraints between the superclass and subclass tables [aren't automatically created](https://jira.grails.org/browse/GRAILS-7729), but will be [as part of Grails 3](https://github.com/grails/grails-data-mapping/pull/10). 