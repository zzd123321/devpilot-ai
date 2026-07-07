create table ask_records (
    id varchar(36) primary key,
    knowledge_base_id varchar(36) not null,
    question text not null,
    answer text not null,
    answer_provider varchar(80) not null,
    source_count integer not null,
    created_at timestamp not null default current_timestamp,
    constraint fk_ask_records_knowledge_base
        foreign key (knowledge_base_id)
        references knowledge_bases (id)
);

create index idx_ask_records_knowledge_base_created_at
    on ask_records (knowledge_base_id, created_at);
