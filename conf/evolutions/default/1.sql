# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table document (
  id                            bigint auto_increment not null,
  deleted                       boolean not null,
  public_id                     varchar(255) not null,
  ymlfile_id                    bigint not null,
  key                           varchar(255) not null,
  orig_text                     text not null,
  trans_text                    text,
  trans_google_text             text,
  remarks                       text,
  status                        integer not null,
  editing                       boolean not null,
  created_date                  timestamp not null,
  modified_date                 timestamp not null,
  constraint uq_document_public_id unique (public_id),
  constraint uq_document_key_ymlfile_id unique (key,ymlfile_id),
  constraint pk_document primary key (id)
);

create table document_edit_log (
  id                            bigint auto_increment not null,
  deleted                       boolean not null,
  document_id                   bigint not null,
  log                           text not null,
  created_date                  timestamp not null,
  modified_date                 timestamp not null,
  constraint pk_document_edit_log primary key (id)
);

create table game (
  id                            bigint auto_increment not null,
  deleted                       boolean not null,
  public_id                     varchar(255) not null,
  name                          varchar(256) not null,
  version                       varchar(255) not null,
  overview                      text,
  created_date                  timestamp not null,
  modified_date                 timestamp not null,
  constraint uq_game_public_id unique (public_id),
  constraint uq_game_name_version unique (name,version),
  constraint pk_game primary key (id)
);

create table game_edit_log (
  id                            bigint auto_increment not null,
  deleted                       boolean not null,
  document_id                   bigint not null,
  log                           text not null,
  created_date                  timestamp not null,
  modified_date                 timestamp not null,
  constraint pk_game_edit_log primary key (id)
);

create table message (
  id                            bigint auto_increment not null,
  deleted                       boolean not null,
  user_id                       bigint not null,
  sender_public_id              varchar(255) not null,
  sender_name                   varchar(255) not null,
  subject                       varchar(255) not null,
  message_text                  varchar(255) not null,
  read                          boolean not null,
  created_date                  timestamp not null,
  modified_date                 timestamp not null,
  constraint pk_message primary key (id)
);

create table pre_regist (
  id                            bigint auto_increment not null,
  deleted                       boolean not null,
  hash                          varchar(127),
  email                         varchar(255) not null,
  user_name                     varchar(255) not null,
  enc_password                  varchar(255) not null,
  expired                       boolean not null,
  user_agent                    varchar(2048) not null,
  remote_ip                     varchar(2048) not null,
  birth_day                     date not null,
  mobile_number                 varchar(255) not null,
  created_date                  timestamp not null,
  modified_date                 timestamp not null,
  constraint uq_pre_regist_hash unique (hash),
  constraint pk_pre_regist primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  deleted                       boolean not null,
  user_name                     varchar(255) not null,
  public_id                     varchar(255) not null,
  enc_password                  varchar(255) not null,
  user_privileges               integer not null,
  user_status                   integer not null,
  birth_day                     date not null,
  mobile_number                 varchar(255) not null,
  mail                          varchar(255) not null,
  created_date                  timestamp not null,
  modified_date                 timestamp not null,
  constraint uq_user_public_id unique (public_id),
  constraint pk_user primary key (id)
);

create table yml_file (
  id                            bigint auto_increment not null,
  deleted                       boolean not null,
  public_id                     varchar(255) not null,
  name                          varchar(256) not null,
  summary                       varchar(256),
  game_id                       bigint not null,
  lang                          integer not null,
  created_date                  timestamp not null,
  modified_date                 timestamp not null,
  constraint uq_yml_file_public_id unique (public_id),
  constraint uq_yml_file_name_game_id unique (name,game_id),
  constraint pk_yml_file primary key (id)
);

create table yml_file_edit_log (
  id                            bigint auto_increment not null,
  deleted                       boolean not null,
  yml_file_id                   bigint not null,
  log                           text not null,
  created_date                  timestamp not null,
  modified_date                 timestamp not null,
  constraint pk_yml_file_edit_log primary key (id)
);

alter table document add constraint fk_document_ymlfile_id foreign key (ymlfile_id) references yml_file (id) on delete restrict on update restrict;
create index ix_document_ymlfile_id on document (ymlfile_id);

alter table document_edit_log add constraint fk_document_edit_log_document_id foreign key (document_id) references document (id) on delete restrict on update restrict;
create index ix_document_edit_log_document_id on document_edit_log (document_id);

alter table game_edit_log add constraint fk_game_edit_log_document_id foreign key (document_id) references document (id) on delete restrict on update restrict;
create index ix_game_edit_log_document_id on game_edit_log (document_id);

alter table message add constraint fk_message_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_message_user_id on message (user_id);

alter table yml_file add constraint fk_yml_file_game_id foreign key (game_id) references game (id) on delete restrict on update restrict;
create index ix_yml_file_game_id on yml_file (game_id);

alter table yml_file_edit_log add constraint fk_yml_file_edit_log_yml_file_id foreign key (yml_file_id) references yml_file (id) on delete restrict on update restrict;
create index ix_yml_file_edit_log_yml_file_id on yml_file_edit_log (yml_file_id);


# --- !Downs

alter table document drop constraint if exists fk_document_ymlfile_id;
drop index if exists ix_document_ymlfile_id;

alter table document_edit_log drop constraint if exists fk_document_edit_log_document_id;
drop index if exists ix_document_edit_log_document_id;

alter table game_edit_log drop constraint if exists fk_game_edit_log_document_id;
drop index if exists ix_game_edit_log_document_id;

alter table message drop constraint if exists fk_message_user_id;
drop index if exists ix_message_user_id;

alter table yml_file drop constraint if exists fk_yml_file_game_id;
drop index if exists ix_yml_file_game_id;

alter table yml_file_edit_log drop constraint if exists fk_yml_file_edit_log_yml_file_id;
drop index if exists ix_yml_file_edit_log_yml_file_id;

drop table if exists document;

drop table if exists document_edit_log;

drop table if exists game;

drop table if exists game_edit_log;

drop table if exists message;

drop table if exists pre_regist;

drop table if exists user;

drop table if exists yml_file;

drop table if exists yml_file_edit_log;

