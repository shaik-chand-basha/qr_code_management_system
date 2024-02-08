

CREATE TABLE user_info (
    user_id BIGINT PRIMARY KEY auto_increment,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    gender ENUM('M', 'F', 'O') NOT NULL,
    password VARCHAR(400) NOT NULL,
    email VARCHAR(50) unique,
    mobile_number VARCHAR(20) unique,
    dob date,
    active BIT DEFAULT 0,
    created_by BIGINT,
    created_at timestamp,
    last_modified_by BIGINT,
    last_modified_at timestamp,
    CONSTRAINT user_info_created_by FOREIGN KEY (created_by)
        REFERENCES user_info (user_id),
    CONSTRAINT user_info_last_modified_by FOREIGN KEY (last_modified_by)
        REFERENCES user_info (user_id)
);

CREATE TABLE image_metadata (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    path_to_image VARCHAR(500) NOT NULL,
    image_type VARCHAR(100),
    created_by BIGINT,
    created_at timestamp,
    last_modified_by BIGINT,
    last_modified_at timestamp,
    CONSTRAINT image_metadata_created_by FOREIGN KEY (created_by)
        REFERENCES user_info (user_id),
    CONSTRAINT image_metadata_last_modified_by FOREIGN KEY (last_modified_by)
        REFERENCES user_info (user_id)
);

alter table user_info add column fk_profile bigint;
alter table user_info add constraint user_info_fk_profile foreign key(fk_profile) references image_metadata(id);

CREATE TABLE user_role (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    user_role VARCHAR(20) NOT NULL,
    active BIT DEFAULT 0
);

CREATE TABLE user_info_role (
    fk_role_id INT,
    fk_user_id BIGINT,
    PRIMARY KEY (fk_role_id , fk_user_id),
    CONSTRAINT user_info_role_fk_role_id FOREIGN KEY (fk_role_id)
        REFERENCES user_role (role_id),
    CONSTRAINT user_info_role_fk_user_id FOREIGN KEY (fk_user_id)
        REFERENCES user_info (user_id)
);

insert into user_role(`user_role`,`active`) values ("ROLE_ADMIN",1),("ROLE_STUDENT",1),("ROLE_INCHARGE",1);

CREATE TABLE email_verification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    fk_user_id BIGINT,
    token VARCHAR(100) NOT NULL,
    created_at timestamp NOT NULL,
    otp_expires timestamp NOT NULL,
    email_verified BIT DEFAULT 0,
    email varchar(100),
    active bit default 0,
    CONSTRAINT email_verification_fk_user_id FOREIGN KEY (fk_user_id)
        REFERENCES user_info (user_id)
);


CREATE TABLE student_details (
    fk_user_id BIGINT primary key,
    address VARCHAR(500),
    hallticket_num VARCHAR(100) unique,
    csi_id VARCHAR(30) unique,
    class VARCHAR(50),
    college varchar(200),
    year_of_join int,
    approved BIT,
    fk_approved_by BIGINT,
    active BIT DEFAULT 0,
    CONSTRAINT student_details_fk_user_id FOREIGN KEY (fk_user_id)
        REFERENCES user_info (user_id),
    created_by BIGINT,
    created_at timestamp,
    last_modified_by BIGINT,
    last_modified_at timestamp,
    CONSTRAINT student_details_created_by FOREIGN KEY (created_by)
        REFERENCES user_info (user_id),
    CONSTRAINT student_details_fk_approved_by FOREIGN KEY (fk_approved_by)
        REFERENCES user_info (user_id),
    CONSTRAINT student_details_last_modified_by FOREIGN KEY (last_modified_by)
        REFERENCES user_info (user_id)
);


CREATE TABLE event_info (
    event_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100),
    description LONGTEXT,
    starting_date DATE,
    ending_date DATE,
    whatsapp_group_link VARCHAR(200),
    venue VARCHAR(200),
    active BIT DEFAULT 0,
    created_by BIGINT,
    created_at TIMESTAMP,
    last_modified_by BIGINT,
    last_modified_at TIMESTAMP,
    fk_profile BIGINT,
    CONSTRAINT event_info_fk_profile FOREIGN KEY (fk_profile)
        REFERENCES image_metadata (id),
    CONSTRAINT event_info_created_by FOREIGN KEY (created_by)
        REFERENCES user_info (user_id),
    CONSTRAINT event_info_last_modified_by FOREIGN KEY (last_modified_by)
        REFERENCES user_info (user_id)
);


CREATE TABLE event_registration (
    fk_event_id BIGINT,
    fk_user_id BIGINT,
    created_by BIGINT,
    created_at TIMESTAMP,
    last_modified_by BIGINT,
    last_modified_at TIMESTAMP,
    PRIMARY KEY (fk_event_id , fk_user_id),
    CONSTRAINT event_registration_fk_event_id FOREIGN KEY (fk_event_id)
        REFERENCES event_info (event_id),
    CONSTRAINT event_registration_fk_user_id FOREIGN KEY (fk_user_id)
        REFERENCES user_info (user_id),
    CONSTRAINT event_registration_created_by FOREIGN KEY (created_by)
        REFERENCES user_info (user_id),
    CONSTRAINT event_registration_last_modified_by FOREIGN KEY (last_modified_by)
        REFERENCES user_info (user_id)
);


CREATE TABLE event_attendence (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    fk_event_id BIGINT,
    fk_user_id BIGINT,
    attendence_datetime timestamp,
    location VARCHAR(100),
    approved BIT DEFAULT 0,
    created_by BIGINT NOT NULL,
    created_at timestamp NOT NULL,
    last_modified_by BIGINT,
    last_modified_at timestamp,
    fk_screenshot BIGINT,
    CONSTRAINT event_attendence_fk_profile FOREIGN KEY (fk_screenshot)
        REFERENCES image_metadata (id),
    CONSTRAINT event_attendence_fk_event_id FOREIGN KEY (fk_event_id)
        REFERENCES event_info (event_id),
    CONSTRAINT event_attendence_fk_user_id FOREIGN KEY (fk_user_id)
        REFERENCES user_info (user_id),
    CONSTRAINT event_attendence_created_by FOREIGN KEY (created_by)
        REFERENCES user_info (user_id),
    CONSTRAINT event_attendence_last_modified_by FOREIGN KEY (last_modified_by)
        REFERENCES user_info (user_id)
);


CREATE TABLE event_photos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    created_by BIGINT,
    created_at timestamp,
    last_modified_by BIGINT,
    last_modified_at timestamp,
    fk_photo BIGINT,
    CONSTRAINT event_photos_fk_profile FOREIGN KEY (fk_photo)
        REFERENCES image_metadata (id),
    CONSTRAINT event_photos_created_by FOREIGN KEY (created_by)
        REFERENCES user_info (user_id),
    CONSTRAINT event_photos_last_modified_by FOREIGN KEY (last_modified_by)
        REFERENCES user_info (user_id)
);


CREATE TABLE authentication_details (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(500) NOT NULL,
	
    refresh_token varchar(500) not null,
    active BIT NOT NULL,
    created_at TIMESTAMP,
    last_modified_at TIMESTAMP,
    fk_user_id BIGINT,
    CONSTRAINT authentication_details_fk_user_id FOREIGN KEY (fk_user_id)
        REFERENCES user_info (user_id)
);
