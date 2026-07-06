create table knowledge_bases (
    id varchar(36) primary key,
    name varchar(80) not null,
    document_count bigint not null default 0,
    created_at timestamp not null default current_timestamp
);

insert into knowledge_bases (id, name, document_count)
values ('demo', 'Demo Knowledge Base', 0);

