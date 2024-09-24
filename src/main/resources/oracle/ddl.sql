CREATE TABLE employee (
    ID NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
    NAME VARCHAR2(500),
    EMAIL VARCHAR2(500),
    STREET_NAME VARCHAR2(500),
    CITY VARCHAR2(500),
    COUNTRY VARCHAR2(500));
CREATE UNIQUE INDEX PK_EMPLOYEE ON EMPLOYEE ("ID");

CREATE GLOBAL TEMPORARY TABLE employee_id (emp_id NUMBER) ON COMMIT PRESERVE ROWS;