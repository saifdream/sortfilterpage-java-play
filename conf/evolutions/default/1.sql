# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table address (
  id                        integer not null,
  house_no                  varchar(255),
  road_no                   varchar(255),
  road_name                 varchar(255),
  apartment_no              varchar(255),
  flat_no                   varchar(255),
  area_name                 varchar(255),
  constraint pk_address primary key (id))
;

create table city (
  id                        integer not null,
  name                      varchar(255),
  short_name                varchar(255),
  address_id                integer,
  constraint pk_city primary key (id))
;

create table role (
  id                        integer not null,
  name                      varchar(255),
  description               varchar(255),
  type                      integer,
  constraint pk_role primary key (id))
;

create table app_user (
  id                        integer not null,
  username                  varchar(18),
  full_name                 varchar(95),
  role_id                   integer,
  dob                       timestamp,
  salary                    decimal(16,2),
  city_id                   integer,
  address_id                integer,
  is_married                boolean,
  gender                    varchar(1),
  constraint pk_app_user primary key (id))
;

create sequence address_seq;

create sequence city_seq;

create sequence role_seq;

create sequence user_seq;

alter table city add constraint fk_city_address_1 foreign key (address_id) references address (id);
create index ix_city_address_1 on city (address_id);
alter table app_user add constraint fk_app_user_role_2 foreign key (role_id) references role (id);
create index ix_app_user_role_2 on app_user (role_id);
alter table app_user add constraint fk_app_user_city_3 foreign key (city_id) references city (id);
create index ix_app_user_city_3 on app_user (city_id);
alter table app_user add constraint fk_app_user_address_4 foreign key (address_id) references address (id);
create index ix_app_user_address_4 on app_user (address_id);



# --- !Downs

drop table if exists address cascade;

drop table if exists city cascade;

drop table if exists role cascade;

drop table if exists app_user cascade;

drop sequence if exists address_seq;

drop sequence if exists city_seq;

drop sequence if exists role_seq;

drop sequence if exists user_seq;

