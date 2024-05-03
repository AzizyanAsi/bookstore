-- initial ACL

-- INSERT ROLES

INSERT INTO role
    (role_type, description)
VALUES ('ADMIN', 'Admin permissions'),
       ('REGULAR_USER', 'System user permissions');
