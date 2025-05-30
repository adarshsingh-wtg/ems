CREATE TABLE Employee (
  Id UUID PRIMARY KEY,
  Name VARCHAR(100) NOT NULL
);

CREATE TABLE Department (
    Id UUID PRIMARY KEY,
    Name VARCHAR(100) NOT NULL UNIQUE,
    ReadOnly BOOLEAN DEFAULT FALSE,
    Mandatory BOOLEAN DEFAULT FALSE
);

CREATE TABLE MapEmployeeDepartment (
   IdEmployee UUID NOT NULL,
   IdDepartment UUID NOT NULL,
   PRIMARY KEY (IdEmployee, IdDepartment),
   CONSTRAINT FK_Employee
       FOREIGN KEY (IdEmployee) REFERENCES Employee (Id),
   CONSTRAINT FK_Department
       FOREIGN KEY (IdDepartment) REFERENCES Department (Id)
);