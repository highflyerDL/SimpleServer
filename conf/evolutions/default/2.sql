# Account schema

# --- !Ups
CREATE TABLE account (
    a_id serial primary key,
    a_customer_id integer NOT NULL,
    a_iban text,
    a_balance numeric(1000,2),
    FOREIGN KEY (a_customer_id) REFERENCES customer(c_id)
);

# --- !Downs
DROP TABLE IF EXISTS account;
