alter table ask_records
    add column prompt_preview text not null default '';

alter table ask_records
    add column sources_json text not null default '[]';
