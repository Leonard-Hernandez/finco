alter table users add column authco_user_id varchar(36) UNIQUE null;
alter table users modify column password VARCHAR(255) null;