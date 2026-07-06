create table document_chunks (
    id varchar(36) primary key,
    knowledge_base_id varchar(36) not null,
    document_id varchar(36) not null,
    chunk_index integer not null,
    content text not null,
    char_start integer not null,
    char_end integer not null,
    created_at timestamp not null default current_timestamp,
    constraint fk_document_chunks_knowledge_base
        foreign key (knowledge_base_id)
        references knowledge_bases (id),
    constraint fk_document_chunks_document
        foreign key (document_id)
        references knowledge_documents (id)
);

create index idx_document_chunks_knowledge_base_id
    on document_chunks (knowledge_base_id);

create index idx_document_chunks_document_id
    on document_chunks (document_id);

