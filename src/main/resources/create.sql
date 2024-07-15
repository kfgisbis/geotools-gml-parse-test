CREATE TABLE public.test_table (
	id serial4 NOT NULL,
	geom public.geometry(geometry, 4326) NULL,
	"desc" text NULL,
	CONSTRAINT test_table_pk PRIMARY KEY (id)
);

create extension if not exists postgis;

INSERT INTO public.test_table (geom,"desc") VALUES
	 (NULL,'empty geom'),
	 ('SRID=4326;POINT (11 48)'::public.geometry,'has geom');
