-- Tabellen
CREATE TABLE surveys (
  id BIGSERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE questions (
  id BIGSERIAL PRIMARY KEY,
  survey_id BIGINT NOT NULL REFERENCES surveys(id) ON DELETE CASCADE,
  text TEXT NOT NULL,
  position INT NOT NULL
);

CREATE TABLE responses (
  id BIGSERIAL PRIMARY KEY,
  survey_id BIGINT NOT NULL REFERENCES surveys(id) ON DELETE CASCADE,
  question_id BIGINT NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
  score INT NOT NULL CHECK (score BETWEEN 1 AND 5),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

INSERT INTO surveys (title) VALUES ('Teamwork-Feedback');

INSERT INTO questions (survey_id, text, position) VALUES
  (1, 'Our team communicates effectively.', 1),
  (1, 'Tasks are distributed fairly.', 2),
  (1, 'Conflicts are resolved constructively.', 3),
  (1, 'Goals are clearly defined.', 4),
  (1, 'I feel supported.', 5);
