-- Table: public.eventos

-- DROP TABLE public.eventos;

CREATE TABLE public.eventos
(
  id integer NOT NULL ,
  sec numeric(8,0) NOT NULL,
  evento character varying(30)  NOT NULL,
  fecha timestamp with time zone NOT NULL,
  processed boolean NOT NULL DEFAULT false,
  CONSTRAINT eventos_pkey PRIMARY KEY (id)
)
;

