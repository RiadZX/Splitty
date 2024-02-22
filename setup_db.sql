DROP TABLE PART_OF IF EXISTS;
DROP TABLE PAID_FOR IF EXISTS;
DROP TABLE DEBTS IF EXISTS;
DROP TABLE IN_EVENT IF EXISTS;
DROP TABLE PAID_IN IF EXISTS;

DROP TABLE EVENTS IF EXISTS;
DROP TABLE PARTICIPANTS IF EXISTS;
DROP TABLE PERSON IF EXISTS;
DROP TABLE QUOTE IF EXISTS;
DROP TABLE EXPENSES IF EXISTS;


CREATE TABLE participants (
    participant_id integer NOT NULL,
    name character varying(50) NOT NULL,
    PRIMARY KEY ( participant_id )
);

CREATE TABLE events (
    event_id integer NOT NULL,
    title character varying(50) NOT NULL,
    event_code character varying(30) NOT NULL,
    date date NOT NULL,
    PRIMARY KEY (event_id)
);

CREATE TABLE expenses (
    expense_id integer NOT NULL,
    event_id integer NOT NULL,
    paid_by integer NOT NULL,
    title character varying(50) NOT NULL,
    amount numeric(10,10) NOT NULL,
    date date NOT NULL,
    PRIMARY KEY (expense_id)
);

CREATE TABLE in_event (
    transaction_id integer NOT NULL,
    participant_id integer NOT NULL,
    event_id integer NOT NULL,
    PRIMARY KEY (transaction_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id),
    FOREIGN KEY (participant_id) REFERENCES participants(participant_id)
);

CREATE TABLE paid_in (
    transaction_id integer NOT NULL,
    expense_id integer NOT NULL,
    event_id integer NOT NULL,
    PRIMARY KEY (transaction_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id),
    FOREIGN KEY (expense_id) REFERENCES expenses(expense_id)
);

CREATE TABLE paid_for (
    transaction_id integer NOT NULL,
    expense_id integer NOT NULL,
    participant_id integer NOT NULL,
    PRIMARY KEY (transaction_id),
    FOREIGN KEY (participant_id) REFERENCES participants(participant_id),
    FOREIGN KEY (expense_id) REFERENCES expenses(expense_id)
);

CREATE TABLE debts (
    debt_id integer NOT NULL,
    expense_id integer NOT NULL,
    amount numeric(10,10) NOT NULL,
    paid boolean DEFAULT false NOT NULL,
    participant_id integer NOT NULL,
    PRIMARY KEY (debt_id),
    FOREIGN KEY (participant_id) REFERENCES participants(participant_id),
    FOREIGN KEY (expense_id) REFERENCES expenses(expense_id)
);

CREATE TABLE part_of (
    part_id integer NOT NULL,
    participant_id integer,
    event_id integer NOT NULL,
    PRIMARY KEY (part_id),
    FOREIGN KEY (participant_id) REFERENCES participants(participant_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id)
);

