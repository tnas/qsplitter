# QSplitter

A simple tool to help developers implement strategies to work around [ORA-01795](https://docs.oracle.com/en/error-help/db/ora-01795/index.html?r=23ai) and [ORA-00913](https://docs.oracle.com/en/error-help/db/ora-00913/?r=23ai) errors.

## Usage

Add this dependency to the `pom.xml`:

```xml
<dependency>
    <groupId>io.github.tnas</groupId>
    <artifactId>qsplitter</artifactId>
    <version>1.0.0</version>
</dependency>
```

The next sections describe how to the three implemented strategies.

### N Queries

```java
static final int TOTAL_RECORDS = 99765;
EntityManager em; // It must be supplied by the application
var ids = LongStream.rangeClosed(1, TOTAL_RECORDS).boxed().collect(Collectors.toList());
var qSplitterDao = new NQueriesDao<User, Long>(em);
var entities = this.qSplitterDao.select(ids, query, User_.id);
assertEquals(503, entities.size());
```

### Disjunctions of Expression Lists

### Temporary Table

## References

Access the DZone article to learn more details about the tool: [Workarounds for Oracle Restrictions on the Size of Expression Lists](https://dzone.com/articles/workarounds-for-oracle-restrictions)