
DROP TABLE IF EXISTS nodes;
CREATE TABLE IF NOT EXISTS nodes(id SERIAL PRIMARY KEY, name text not null, parent INTEGER);
CREATE INDEX IF NOT EXISTS parent_brin_index ON nodes USING BRIN(parent) WITH (pages_per_range = 128);
