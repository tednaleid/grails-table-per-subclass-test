dataSource {
    pooled = true
    // looks like Grails 3 adds configuration to support cascading deletes:
    // configClass = 'TablePerSubclassConfiguration'
    driverClassName = "com.mysql.jdbc.Driver"
    dialect = org.hibernate.dialect.MySQL5InnoDBDialect
    url = "jdbc:mysql://localhost/tablepersubclass?useUnicode=yes&characterEncoding=UTF-8"
    username = "tablepersubclass"
    password = "tablepersubclass"
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
//    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
}

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:mysql://localhost/tablepersubclass_test?useUnicode=yes&characterEncoding=UTF-8"
        }
    }
}
