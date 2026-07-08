-- Schema for Room database: todo_db
-- Tables must match Room-generated schema exactly.

PRAGMA user_version = 1;

CREATE TABLE IF NOT EXISTS `users` (
    `id`    INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    `name`  TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS `todos` (
    `id`                INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    `uuid`              TEXT NOT NULL,
    `title`             TEXT NOT NULL,
    `status`            INTEGER NOT NULL,
    `priority`          INTEGER NOT NULL,
    `group`             INTEGER NOT NULL,
    `deadlineTimestamp` INTEGER NOT NULL
);

-- Seed data: users
INSERT INTO `users` (`name`) VALUES ('Default User');
INSERT INTO `users` (`name`) VALUES ('Work Account');

-- Seed data: todos (uuid, title, status, priority, group, deadlineTimestamp)
-- status ordinal: NOT_STARTED=1, IN_PROGRESS=2, COMPLETED=3
-- priority ordinal: LOW=0, MID=1, HIGH=2
-- group ordinal: LIFE=0, WORK=1, EXERCISE=2, GAME=3
INSERT INTO `todos` (`uuid`, `title`, `status`, `priority`, `group`, `deadlineTimestamp`)
VALUES ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'Buy groceries', 3, 0, 0, 1725000000000);

INSERT INTO `todos` (`uuid`, `title`, `status`, `priority`, `group`, `deadlineTimestamp`)
VALUES ('b2c3d4e5-f6a7-8901-bcde-f12345678901', 'Finish project report', 1, 2, 1, 1740000000000);

INSERT INTO `todos` (`uuid`, `title`, `status`, `priority`, `group`, `deadlineTimestamp`)
VALUES ('c3d4e5f6-a7b8-9012-cdef-123456789012', 'Morning jog', 2, 1, 2, 1728000000000);

INSERT INTO `todos` (`uuid`, `title`, `status`, `priority`, `group`, `deadlineTimestamp`)
VALUES ('d4e5f6a7-b8c9-0123-defa-234567890123', 'Play chess online', 4, 0, 3, 1730000000000);

INSERT INTO `todos` (`uuid`, `title`, `status`, `priority`, `group`, `deadlineTimestamp`)
VALUES ('e5f6a7b8-c9d0-1234-efab-345678901234', 'Read Clean Code', 1, 1, 0, 1745000000000);

INSERT INTO `todos` (`uuid`, `title`, `status`, `priority`, `group`, `deadlineTimestamp`)
VALUES ('f6a7b8c9-d0e1-2345-fabc-456789012345', 'Team standup meeting', 3, 2, 1, 1722000000000);