INSERT INTO ROLES (
	ID, 
	CREATED_AT, 
	LAST_MODIFIED_AT, 
	NAME
) VALUES (
	UNHEX(REPLACE(UUID(), '-', '')), 
	CURRENT_TIMESTAMP,
	NULL,
	'Customer'
);

INSERT INTO ROLES (
	ID, 
	CREATED_AT, 
	LAST_MODIFIED_AT, 
	NAME
) VALUES (
	UNHEX(REPLACE(UUID(), '-', '')), 
	CURRENT_TIMESTAMP,
	NULL,
	'Employee'
);

INSERT INTO USERS (
	ID,
	FIRST_NAME,
	LAST_NAME,
	CREATED_AT,
	LAST_MODIFIED_AT,
	EMAIL,
	LOCKED,
	PASSWORD,
	USERNAME,
	EMAIL_CONFIRMED
) VALUES (
	UNHEX(REPLACE(UUID(), '-', '')),
	'John',
	'Doe',
	CURRENT_TIMESTAMP,
	NULL,
	'john.doe@bookify.com',
	0,
	'$2a$10$rtso7kYOpaWDdAgT51eMMemZAnXWVGUUAIoQF6Lp3kqSKpLQ9Tk2u', -- employee123#1
	'john.doe',
	1
);

INSERT INTO USER_ROLE_MAP (
	USER_ID,
	ROLE_ID
) VALUES (
	(SELECT ID FROM USERS WHERE USERNAME = 'john.doe'),
	(SELECT ID FROM ROLES WHERE NAME = 'Employee')
);


/*
INSERT INTO roles (
    id,
    created_at,
    last_modified_at,
    name
) VALUES (
             UNHEX(REPLACE(UUID(), '-', '')),
             CURRENT_TIMESTAMP,
             NULL,
             'customer'
         );

INSERT INTO roles (
    id,
    created_at,
    last_modified_at,
    name
) VALUES (
             UNHEX(REPLACE(UUID(), '-', '')),
             CURRENT_TIMESTAMP,
             NULL,
             'employee'
         );


INSERT INTO users (
    id,
    first_name,
    last_name,
    created_at,
    last_modified_at,
    email,
    locked,
    password,
    username,
    email_confirmed
) VALUES (
             UNHEX(REPLACE(UUID(), '-', '')),
             'john',
             'doe',
             CURRENT_TIMESTAMP,
             NULL,
             'john.doe@bookify.com',
             0,
             '$2a$10$rtso7kYOpaWDdAgT51eMMemZAnXWVGUUAIoQF6Lp3kqSKpLQ9Tk2u',
             'john.doe',
             1
         );


INSERT INTO user_role_map (
    user_id,
    role_id
) VALUES (
             (SELECT id FROM users WHERE username = 'john.doe'),
             (SELECT id FROM roles WHERE name = 'employee')
         );
*/