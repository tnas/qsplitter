# QSplitter

A simple tool to help developers implement strategies to work around [ORA-01795](https://docs.oracle.com/en/error-help/db/ora-01795/index.html?r=23ai) and [ORA-00913](https://docs.oracle.com/en/error-help/db/ora-00913/?r=23ai) errors.

Six strategies were implemented:
- N Queries
- Disjunctions of Expression Lists
- Disjunctions of IDs
- Temporary Table
- Union All of Expression Lists
- Multivalued Expression List