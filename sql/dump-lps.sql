--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.3
-- Dumped by pg_dump version 14.2

-- Started on 2022-08-17 02:12:56

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

--
-- TOC entry 2162 (class 1262 OID 16395)
-- Name: lps; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE lps WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'English_United States.1252';


\connect lps

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

--
-- TOC entry 6 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA public;


--
-- TOC entry 2163 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 555 (class 1247 OID 16397)
-- Name: gender; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.gender AS ENUM (
    'male',
    'female',
    'other'
);


--
-- TOC entry 558 (class 1247 OID 16404)
-- Name: loan_state; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.loan_state AS ENUM (
    'created',
    'rejected',
    'approved',
    'signed',
    'completed'
);


--
-- TOC entry 561 (class 1247 OID 16416)
-- Name: marital_status; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.marital_status AS ENUM (
    'single',
    'married'
);


--
-- TOC entry 564 (class 1247 OID 16422)
-- Name: user_role; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.user_role AS ENUM (
    'admin',
    'moderator',
    'manager',
    'client'
);


--
-- TOC entry 188 (class 1259 OID 16482)
-- Name: credit_application_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.credit_application_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


SET default_tablespace = '';

--
-- TOC entry 181 (class 1259 OID 16431)
-- Name: credit_application; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.credit_application (
    id integer DEFAULT nextval('public.credit_application_id_seq'::regclass) NOT NULL,
    desired_amount numeric NOT NULL,
    user_id integer NOT NULL,
    repayment_time smallint,
    comment text,
    loan_state public.loan_state DEFAULT 'created'::public.loan_state,
    uuid character varying(36),
    creation_date date DEFAULT now(),
    approved_amount numeric,
    contact_number character varying(11),
    passport_id integer,
    job_id integer,
    sign_date date
);


--
-- TOC entry 2165 (class 0 OID 0)
-- Dependencies: 181
-- Name: COLUMN credit_application.repayment_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.credit_application.repayment_time IS 'Number in days';


--
-- TOC entry 187 (class 1259 OID 16480)
-- Name: job_place_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.job_place_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 182 (class 1259 OID 16438)
-- Name: job_place; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.job_place (
    id integer DEFAULT nextval('public.job_place_id_seq'::regclass) NOT NULL,
    company_name character varying(25) NOT NULL,
    job_title character varying(25) NOT NULL,
    period_start date NOT NULL,
    period_end date,
    user_id integer NOT NULL,
    salary integer
);


--
-- TOC entry 185 (class 1259 OID 16476)
-- Name: passport_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.passport_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 183 (class 1259 OID 16441)
-- Name: passport; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.passport (
    passport_id character varying(10) NOT NULL,
    passport_series character varying(5) NOT NULL,
    second_name character varying(35) NOT NULL,
    patronymic character varying(35),
    first_name character varying(25) NOT NULL,
    marital_status public.marital_status DEFAULT 'single'::public.marital_status NOT NULL,
    birthdate date NOT NULL,
    id integer DEFAULT nextval('public.passport_id_seq'::regclass) NOT NULL,
    user_id integer NOT NULL,
    gender public.gender NOT NULL,
    registration_city text NOT NULL,
    registration_country text NOT NULL,
    registration_street text NOT NULL,
    registration_apps character varying(8),
    registration_house character varying(8) NOT NULL,
    registration_region character varying(25),
    issuedby text NOT NULL,
    passport_date date
);


--
-- TOC entry 186 (class 1259 OID 16478)
-- Name: user_data_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.user_data_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 184 (class 1259 OID 16448)
-- Name: user_data; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_data (
    id integer DEFAULT nextval('public.user_data_id_seq'::regclass) NOT NULL,
    role public.user_role DEFAULT 'client'::public.user_role NOT NULL,
    active boolean DEFAULT false,
    comment text,
    username character varying,
    password character varying
);


--
-- TOC entry 2149 (class 0 OID 16431)
-- Dependencies: 181
-- Data for Name: credit_application; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.credit_application VALUES (2, 180000, 6, 370, NULL, 'completed', '8c79cf3d-599c-4d04-bbdd-ba4e0e037d1a', '2022-08-11', 190000, '89065372841', 6, 4, '2022-08-12');
INSERT INTO public.credit_application VALUES (10, 450000, 24, 1200, '', 'signed', '6565e151-4eb1-4f33-bb12-6ad6c5934dfd', '2022-08-14', 450000, '79875553272', 14, 16, '2022-08-15');
INSERT INTO public.credit_application VALUES (3, 700000, 7, 1200, NULL, 'signed', 'eaad4e80-1990-11ed-861d-0242ac120002', '2022-08-11', 700000, '88443256199', 7, 3, '2022-08-16');
INSERT INTO public.credit_application VALUES (12, 90000, 25, 150, '', 'completed', 'd0a0cf63-5f15-4f1f-9bd1-1766c9f17955', '2022-08-15', 90000, '79876559028', 15, 18, '2022-08-15');
INSERT INTO public.credit_application VALUES (14, 900000, 27, 60, '', 'signed', '6706d8d9-1aa3-4f54-b54c-8bd4bfab1492', '2022-08-16', 900000, '79098762755', 17, 20, '2022-08-16');
INSERT INTO public.credit_application VALUES (13, 130000, 26, 30, '', 'rejected', 'e556af63-52a5-45f9-ae5e-84cffcf59113', '2022-08-15', 130000, '79096551451', 16, 19, '2022-08-15');
INSERT INTO public.credit_application VALUES (15, 390000, 28, 360, '', 'signed', '03fac4f9-1246-419d-8d15-282282f8d81f', '2022-08-17', 390000, '79876662895', 18, 21, '2022-08-17');
INSERT INTO public.credit_application VALUES (1, 245000, 5, 180, '', 'approved', '9f1de747-e1f3-460f-8143-9a2fbea54c6f', '2022-08-11', 220000, '89876452876', 5, 1, '2022-08-15');
INSERT INTO public.credit_application VALUES (11, 120000, 24, 60, '', 'completed', 'c54a569d-586f-4134-af2f-01ed7a41464e', '2022-08-14', 120000, '79876551317', 14, 17, '2022-08-15');


--
-- TOC entry 2150 (class 0 OID 16438)
-- Dependencies: 182
-- Data for Name: job_place; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.job_place VALUES (2, 'Magnit', 'Salesman', '2020-08-01', '2022-02-01', 6, 25000);
INSERT INTO public.job_place VALUES (3, 'Chirai gorod', 'Manager', '2019-01-13', NULL, 7, 45000);
INSERT INTO public.job_place VALUES (4, 'Magnit', 'Director', '2022-02-01', NULL, 6, 52300);
INSERT INTO public.job_place VALUES (1, 'Volgograd energosbyt', 'Software engineer', '2022-08-01', NULL, 5, 22000);
INSERT INTO public.job_place VALUES (6, 'ООО добрострой', 'Разнорабочий', '2022-08-12', NULL, 5, 35000);
INSERT INTO public.job_place VALUES (16, 'ООО ФитнесСервис', 'Менеджер', '2014-01-01', '1970-01-01', 24, 35000);
INSERT INTO public.job_place VALUES (17, '', '', '1970-01-01', '1970-01-01', 24, 0);
INSERT INTO public.job_place VALUES (18, 'ООО Тандер', 'Кассир', '2022-02-02', '1970-01-01', 25, 24600);
INSERT INTO public.job_place VALUES (19, 'Лента', 'Кассир', '2019-02-05', '1970-01-01', 26, 32000);
INSERT INTO public.job_place VALUES (20, 'Софтек', 'Разработчик', '2010-08-04', '1970-01-01', 27, 305000);
INSERT INTO public.job_place VALUES (21, 'ООО АиР', 'Разработчик БД', '2005-09-06', '1970-01-01', 28, 120000);


--
-- TOC entry 2151 (class 0 OID 16441)
-- Dependencies: 183
-- Data for Name: passport; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.passport VALUES ('635 711', '14-17', 'Ivanov', 'Dmitrievich', 'Igor', 'single', '1997-05-14', 5, 5, 'male', 'Kolomna', 'Russia', 'Lenina', '35', '11', 'Moscow Oblast', 'Отделом МВД РФ по г. Коломна', '2017-06-06');
INSERT INTO public.passport VALUES ('823 092', '19-15', 'Nikolaeva', 'Vasilievna', 'Anna', 'single', '1994-02-16', 6, 6, 'female', 'Volzsky', 'Russia', 'Pushkina', '26', '15', 'Volgograd Oblast', 'Управлением МВД по Волгоградской области', '2014-03-05');
INSERT INTO public.passport VALUES ('549 255', '16-12', 'Petrov', 'Alekseevich', 'Anton', 'married', '1996-01-26', 7, 7, 'male', 'Volgograd', 'Russia', 'Chkalova', '28', '11', 'Volgograd Oblast', 'Отделом МВД РФ во г. Волгоград', '2016-01-28');
INSERT INTO public.passport VALUES ('650750', '1817', 'Макаренко', '', 'Антон', 'married', '1990-01-01', 14, 24, 'male', 'Волгоград', 'РФ', 'Ленина', '', '117А', 'Волгоградская область', 'Отделом МВД РФ по г. Волгограду', '2012-04-05');
INSERT INTO public.passport VALUES ('876900', '2021', 'Иванченко', 'Владимирович', 'Антон', 'single', '2003-09-10', 15, 25, 'male', 'Волжский', 'Российская Федерация', 'Мира', '49', '9Б', 'Волгоградская область', 'Отделом МВД по Волгоградской области, г. Волгоград', '2017-09-15');
INSERT INTO public.passport VALUES ('190785', '1913', 'Катков', 'Владимирович', 'Дмитрий', 'married', '1995-02-07', 16, 26, 'male', 'Калач-На-Дону', 'РФ', '9-Мая', '', '13', 'Волгоградская область', 'Отделом МВД по г. Волжский', '2005-04-05');
INSERT INTO public.passport VALUES ('125483', '0965', 'Иван', 'Николаевич', 'Голубев', 'single', '1980-02-06', 17, 27, 'male', 'Ср. Ахтуба', 'РФ', 'Ленина', '32', '55В', 'Волгоградская область', 'Отделом МВД по г. Волжский', '2000-01-01');
INSERT INTO public.passport VALUES ('334527', '0918', 'Рогатина', 'Борисовна', 'Евгения', 'married', '1982-07-01', 18, 28, 'female', 'Балашиха', 'Российская Федерация', 'Ленина', '199', '145А', 'Московская область', 'Отделом МВД по г. Балашиха', '2003-07-05');


--
-- TOC entry 2152 (class 0 OID 16448)
-- Dependencies: 184
-- Data for Name: user_data; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.user_data VALUES (2, 'admin', false, NULL, NULL, NULL);
INSERT INTO public.user_data VALUES (4, 'manager', false, NULL, NULL, NULL);
INSERT INTO public.user_data VALUES (5, 'client', false, NULL, NULL, NULL);
INSERT INTO public.user_data VALUES (6, 'client', false, NULL, NULL, NULL);
INSERT INTO public.user_data VALUES (7, 'client', false, NULL, NULL, NULL);
INSERT INTO public.user_data VALUES (24, 'client', false, '', NULL, NULL);
INSERT INTO public.user_data VALUES (3, 'manager', true, NULL, 'IvanPetrov', 'rgijw82529h');
INSERT INTO public.user_data VALUES (25, 'client', false, '', NULL, NULL);
INSERT INTO public.user_data VALUES (26, 'client', false, '', NULL, NULL);
INSERT INTO public.user_data VALUES (27, 'client', false, '', NULL, NULL);
INSERT INTO public.user_data VALUES (28, 'client', false, '', NULL, NULL);


--
-- TOC entry 2166 (class 0 OID 0)
-- Dependencies: 188
-- Name: credit_application_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.credit_application_id_seq', 15, true);


--
-- TOC entry 2167 (class 0 OID 0)
-- Dependencies: 187
-- Name: job_place_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.job_place_id_seq', 21, true);


--
-- TOC entry 2168 (class 0 OID 0)
-- Dependencies: 185
-- Name: passport_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.passport_id_seq', 18, true);


--
-- TOC entry 2169 (class 0 OID 0)
-- Dependencies: 186
-- Name: user_data_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.user_data_id_seq', 28, true);


--
-- TOC entry 2024 (class 2606 OID 16453)
-- Name: credit_application credit_application_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.credit_application
    ADD CONSTRAINT credit_application_pk PRIMARY KEY (id);


--
-- TOC entry 2026 (class 2606 OID 16455)
-- Name: job_place job_places_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_place
    ADD CONSTRAINT job_places_pk PRIMARY KEY (id);


--
-- TOC entry 2029 (class 2606 OID 16457)
-- Name: passport passport_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.passport
    ADD CONSTRAINT passport_pk PRIMARY KEY (id);


--
-- TOC entry 2031 (class 2606 OID 16459)
-- Name: user_data user_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_data
    ADD CONSTRAINT user_pk PRIMARY KEY (id);


--
-- TOC entry 2027 (class 1259 OID 16499)
-- Name: passport_passport_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX passport_passport_id_idx ON public.passport USING btree (passport_id, passport_series);


--
-- TOC entry 2032 (class 2606 OID 16461)
-- Name: credit_application credit_application_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.credit_application
    ADD CONSTRAINT credit_application_fk FOREIGN KEY (user_id) REFERENCES public.user_data(id);


--
-- TOC entry 2033 (class 2606 OID 16466)
-- Name: job_place job_places_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.job_place
    ADD CONSTRAINT job_places_fk FOREIGN KEY (user_id) REFERENCES public.user_data(id);


--
-- TOC entry 2034 (class 2606 OID 16471)
-- Name: passport passport_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.passport
    ADD CONSTRAINT passport_fk FOREIGN KEY (user_id) REFERENCES public.user_data(id);


--
-- TOC entry 2164 (class 0 OID 0)
-- Dependencies: 6
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2022-08-17 02:12:57

--
-- PostgreSQL database dump complete
--

