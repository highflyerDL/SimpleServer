# Customer schema

# --- !Ups
CREATE TABLE customer (
    c_id serial primary key,
    c_first_name text,
    c_last_name text,
    c_email text
);

# --- !Downs
DROP TABLE IF EXISTS customer;
