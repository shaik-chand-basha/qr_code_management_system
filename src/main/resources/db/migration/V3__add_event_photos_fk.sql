alter table event_photos add column fk_event_id bigint;
alter table event_photos add constraint event_photos_fk_event_id foreign key(fk_event_id) references event_info(event_id);
