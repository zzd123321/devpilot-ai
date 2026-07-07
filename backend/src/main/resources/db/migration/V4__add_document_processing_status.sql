alter table knowledge_documents
    add column processing_status varchar(32) not null default 'READY';

alter table knowledge_documents
    add column processing_error varchar(500);
