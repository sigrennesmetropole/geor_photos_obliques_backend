create table photos_obliques.feature (id  bigserial not null, uuid uuid not null, code varchar(30) not null, label varchar(100), closing_date timestamp, opening_date timestamp not null, order_ int4 not null, available_scope varchar(50) not null, primary key (id));
create table photos_obliques.role (id  bigserial not null, uuid uuid not null, code varchar(30) not null, label varchar(100), closing_date timestamp, opening_date timestamp not null, order_ int4 not null, code_ldap varchar(100), primary key (id));
create table photos_obliques.role_feature (id  bigserial not null, uuid uuid not null, scope varchar(50), feature_fk int8, role_fk int8, primary key (id));
create table photos_obliques.user_ (id  bigserial not null, uuid uuid not null, firstname varchar(30), lastname varchar(30), login varchar(100) not null, type varchar(50) not null, email varchar(255), primary key (id));

create table photos_obliques.refresh_token (id  bigserial not null, token varchar(2048) not null, primary key (id));

alter table if exists photos_obliques.feature add constraint UK_a0qmi8htvty1idedoo8dlxf99 unique (uuid);
alter table if exists photos_obliques.role add constraint UK_k5dwya5n8n7y3m2opvmm7qjcc unique (uuid);
alter table if exists photos_obliques.role_feature add constraint UK_bnr4034xj5myyw6lefsp6oxny unique (uuid);
alter table if exists photos_obliques.user_ add constraint UK_1xc1iry6gqjrvh5cpajiq7l2f unique (uuid);
alter table if exists photos_obliques.role_feature add constraint FKg9bc8u1c7js8trey06esd5bf8 foreign key (feature_fk) references photos_obliques.feature;
alter table if exists photos_obliques.role_feature add constraint FKn88me5qkw1u2x03yvlxvyq0eo foreign key (role_fk) references photos_obliques.role;

create table if not exists photos_obliques.async_information (id  bigserial not null, uuid uuid not null,
	login varchar(50) not null, scope varchar(255), progression int4 not null,
	status varchar(20) not null, creation_date timestamp, message varchar(1024), content_type varchar(100),
	file_name varchar(255), file_path varchar(255), primary key (id));

create table photos_obliques.user_custom_configuration (id  bigserial not null, uuid uuid not null, login varchar(100) not null, properties TEXT, primary key (id));

alter table if exists photos_obliques.user_custom_configuration add constraint UK_egu6x5h6b30bcog32muvt0k4g unique (uuid);

create table photos_obliques.historic_data (id  bigserial not null, uuid uuid not null, data text, what varchar(50) not null, when_ timestamp not null, which uuid not null, who varchar(100) not null,  full_who varchar(100) not null default '', primary key (id));
alter table if exists photos_obliques.historic_data add constraint UK_q8n5t9mvitbq2bpsvsv5t3p8v unique (uuid);
alter table photos_obliques.historic_data add column IF NOT EXISTS which_type varchar(256) not null default '';

CREATE INDEX if not exists idx1_historic_data ON photos_obliques.historic_data (when_);
CREATE INDEX if not exists idx2_historic_data ON photos_obliques.historic_data (what);
CREATE INDEX if not exists idx3_historic_data ON photos_obliques.historic_data (which);
CREATE INDEX if not exists idx4_historic_data ON photos_obliques.historic_data (which_type);
CREATE INDEX if not exists idx5_historic_data ON photos_obliques.historic_data (which,which_type);
CREATE INDEX if not exists idx6_historic_data ON photos_obliques.historic_data (which,which_type,what);

create table if not exists photos_obliques.sequence_counter (
	id  bigserial not null, uuid uuid not null,
	code varchar(50) not null, annee int8 not null, value_ int8 not null, primary key (id)
);

alter table if exists photos_obliques.sequence_counter add constraint sequence_counter_code_uniq unique (code);
alter table if exists photos_obliques.sequence_counter add constraint sequence_counter_uniq unique (uuid);
