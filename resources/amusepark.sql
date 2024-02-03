-- Schema that represents the database structure
-- Syntax: SQLite

-- Drop tables if they already exist
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS attractions;
DROP TABLE IF EXISTS memberships;
DROP TABLE IF EXISTS bookings;

-- Table: customers
CREATE TABLE IF NOT EXISTS customers
(
    fiscal_code TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    surname     TEXT NOT NULL
);

-- Table: employes
CREATE TABLE IF NOT EXISTS employees
(
    fiscal_code TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    surname     TEXT NOT NULL,
    salary      REAL NOT NULL
);

-- Table: attractions
CREATE TABLE IF NOT EXISTS attractions
(
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    name         TEXT    NOT NULL,
    max_capacity INTEGER NOT NULL,
    start_date   TEXT    NOT NULL,
    end_date     TEXT    NOT NULL,
    employee      TEXT    NOT NULL,
    FOREIGN KEY (employee) REFERENCES employees (fiscal_code) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Table: memberships
CREATE TABLE IF NOT EXISTS  memberships
(
	customer	TEXT,
	valid_from	TEXT NOT NULL,
	valid_until	TEXT NOT NULL,
	PRIMARY KEY(customer),
	FOREIGN KEY(customer) REFERENCES customers(fiscal_code) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Table: memberships_extensions
CREATE TABLE IF NOT EXISTS memberships_extensions
(
    customer TEXT    NOT NULL,
    type     TEXT    NOT NULL, -- Type of extension (e.g. "silver")
    uses     INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (customer, type),
    FOREIGN KEY (customer) REFERENCES tickets (customer) ON UPDATE CASCADE ON DELETE CASCADE
);


-- Table: bookings
CREATE TABLE IF NOT EXISTS bookings
(
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    customer TEXT    NOT NULL,
    type     TEXT    NOT NULL, -- Type of bookings (e.g. "flashpark, parking,restaurant")
    datebook   TEXT    NOT NULL,
    hourbook     TEXT    NOT NULL,
    FOREIGN KEY (customer) REFERENCES customers (fiscal_code) ON UPDATE CASCADE ON DELETE CASCADE
);

insert into employees values ('PTRMTH','Mathilde','PAT',10000);
insert into attractions values (1,'Starlight',100,'01-01-2023','31-12-2023',1);