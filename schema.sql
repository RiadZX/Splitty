--
-- PostgreSQL database dump
--

-- Dumped from database version 15.5
-- Dumped by pg_dump version 15.5

-- Started on 2024-02-20 21:55:09

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 221 (class 1259 OID 24633)
-- Name: debts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.debts (
    debt_id integer NOT NULL,
    expense_id integer NOT NULL,
    amount numeric(10,10) NOT NULL,
    paid boolean DEFAULT false NOT NULL,
    participant_id integer NOT NULL
);


ALTER TABLE public.debts OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 24632)
-- Name: debts_debt_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.debts_debt_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.debts_debt_id_seq OWNER TO postgres;

--
-- TOC entry 3361 (class 0 OID 0)
-- Dependencies: 220
-- Name: debts_debt_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.debts_debt_id_seq OWNED BY public.debts.debt_id;


--
-- TOC entry 215 (class 1259 OID 24597)
-- Name: events; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.events (
    event_id integer NOT NULL,
    title character varying(50) NOT NULL,
    event_code character varying(30) NOT NULL,
    date date NOT NULL
);


ALTER TABLE public.events OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 24596)
-- Name: events_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.events_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.events_id_seq OWNER TO postgres;

--
-- TOC entry 3362 (class 0 OID 0)
-- Dependencies: 214
-- Name: events_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.events_id_seq OWNED BY public.events.event_id;


--
-- TOC entry 219 (class 1259 OID 24616)
-- Name: expenses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.expenses (
    expense_id integer NOT NULL,
    event_id integer NOT NULL,
    paid_by integer NOT NULL,
    title character varying(50) NOT NULL,
    amount numeric(10,10) NOT NULL,
    date date NOT NULL
);


ALTER TABLE public.expenses OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 24615)
-- Name: expenses_expense_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.expenses_expense_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.expenses_expense_id_seq OWNER TO postgres;

--
-- TOC entry 3363 (class 0 OID 0)
-- Dependencies: 218
-- Name: expenses_expense_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.expenses_expense_id_seq OWNED BY public.expenses.expense_id;


--
-- TOC entry 217 (class 1259 OID 24604)
-- Name: participants; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.participants (
    participant_id integer NOT NULL,
    event_id integer NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE public.participants OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 24603)
-- Name: participants_participant_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.participants_participant_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.participants_participant_id_seq OWNER TO postgres;

--
-- TOC entry 3364 (class 0 OID 0)
-- Dependencies: 216
-- Name: participants_participant_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.participants_participant_id_seq OWNED BY public.participants.participant_id;


--
-- TOC entry 3191 (class 2604 OID 24636)
-- Name: debts debt_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.debts ALTER COLUMN debt_id SET DEFAULT nextval('public.debts_debt_id_seq'::regclass);


--
-- TOC entry 3188 (class 2604 OID 24600)
-- Name: events event_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events ALTER COLUMN event_id SET DEFAULT nextval('public.events_id_seq'::regclass);


--
-- TOC entry 3190 (class 2604 OID 24619)
-- Name: expenses expense_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expenses ALTER COLUMN expense_id SET DEFAULT nextval('public.expenses_expense_id_seq'::regclass);


--
-- TOC entry 3189 (class 2604 OID 24607)
-- Name: participants participant_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.participants ALTER COLUMN participant_id SET DEFAULT nextval('public.participants_participant_id_seq'::regclass);


--
-- TOC entry 3355 (class 0 OID 24633)
-- Dependencies: 221
-- Data for Name: debts; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3349 (class 0 OID 24597)
-- Dependencies: 215
-- Data for Name: events; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3353 (class 0 OID 24616)
-- Dependencies: 219
-- Data for Name: expenses; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3351 (class 0 OID 24604)
-- Dependencies: 217
-- Data for Name: participants; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3365 (class 0 OID 0)
-- Dependencies: 220
-- Name: debts_debt_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.debts_debt_id_seq', 1, false);


--
-- TOC entry 3366 (class 0 OID 0)
-- Dependencies: 214
-- Name: events_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.events_id_seq', 1, false);


--
-- TOC entry 3367 (class 0 OID 0)
-- Dependencies: 218
-- Name: expenses_expense_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.expenses_expense_id_seq', 1, false);


--
-- TOC entry 3368 (class 0 OID 0)
-- Dependencies: 216
-- Name: participants_participant_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.participants_participant_id_seq', 1, false);


--
-- TOC entry 3200 (class 2606 OID 24639)
-- Name: debts debts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.debts
    ADD CONSTRAINT debts_pkey PRIMARY KEY (debt_id);


--
-- TOC entry 3194 (class 2606 OID 24602)
-- Name: events events_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_pkey PRIMARY KEY (event_id);


--
-- TOC entry 3198 (class 2606 OID 24621)
-- Name: expenses expenses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expenses
    ADD CONSTRAINT expenses_pkey PRIMARY KEY (expense_id);


--
-- TOC entry 3196 (class 2606 OID 24609)
-- Name: participants participants_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.participants
    ADD CONSTRAINT participants_pkey PRIMARY KEY (participant_id);


--
-- TOC entry 3204 (class 2606 OID 24640)
-- Name: debts debts_expense_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.debts
    ADD CONSTRAINT debts_expense_id_fkey FOREIGN KEY (expense_id) REFERENCES public.expenses(expense_id);


--
-- TOC entry 3205 (class 2606 OID 24645)
-- Name: debts debts_participant_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.debts
    ADD CONSTRAINT debts_participant_id_fkey FOREIGN KEY (participant_id) REFERENCES public.participants(participant_id);


--
-- TOC entry 3201 (class 2606 OID 24610)
-- Name: participants event_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.participants
    ADD CONSTRAINT event_id FOREIGN KEY (event_id) REFERENCES public.events(event_id);


--
-- TOC entry 3202 (class 2606 OID 24627)
-- Name: expenses event_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expenses
    ADD CONSTRAINT event_id FOREIGN KEY (event_id) REFERENCES public.events(event_id);


--
-- TOC entry 3203 (class 2606 OID 24622)
-- Name: expenses paid_by; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.expenses
    ADD CONSTRAINT paid_by FOREIGN KEY (paid_by) REFERENCES public.participants(participant_id);


-- Completed on 2024-02-20 21:55:10

--
-- PostgreSQL database dump complete
--

