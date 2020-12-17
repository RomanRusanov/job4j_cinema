CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    fio TEXT,
    phone TEXT
);
CREATE TABLE halls (
    id SERIAL PRIMARY KEY,
    name TEXT
);
CREATE TABLE hall (
   id SERIAL,
   row NUMERIC not null,
   place NUMERIC not null,
   free BOOLEAN,
   account_id NUMERIC,
   hall_id NUMERIC,
   PRIMARY KEY(row, place)
);
-- Fill DB
insert into halls (name) values ('Главный зал');
insert into hall (row, place, free, hall_id)
    values
           (1, 1, true , 1),
           (1, 2, true , 1),
           (1, 3, true , 1),
           (2, 1, true , 1),
           (2, 2, true , 1),
           (2, 3, true , 1),
           (3, 1, true , 1),
           (3, 2, true , 1),
           (3, 3, true , 1);