drop table if exists public.MOCK_DATA;

create table public.MOCK_DATA (
	id INT,
	sec VARCHAR(50),
	evento VARCHAR(500),
	fecha DATE,
	processed boolean
);

