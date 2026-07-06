create table knowledge_documents (
    id varchar(36) primary key,
    knowledge_base_id varchar(36) not null,
    filename varchar(255) not null,
    content_type varchar(120),
    size_bytes bigint not null,
    content text not null,
    created_at timestamp not null default current_timestamp,
    constraint fk_knowledge_documents_knowledge_base
        foreign key (knowledge_base_id)
        references knowledge_bases (id)
);

create index idx_knowledge_documents_knowledge_base_id
    on knowledge_documents (knowledge_base_id);

