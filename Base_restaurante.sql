PGDMP      .                |            restaurante_bd    16.4    16.4 3               0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    16670    restaurante_bd    DATABASE     �   CREATE DATABASE restaurante_bd WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Spanish_Mexico.1252';
    DROP DATABASE restaurante_bd;
                postgres    false                        3079    16777    pgcrypto 	   EXTENSION     <   CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;
    DROP EXTENSION pgcrypto;
                   false                       0    0    EXTENSION pgcrypto    COMMENT     <   COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';
                        false    2            �            1259    16751    carrito    TABLE     �   CREATE TABLE public.carrito (
    id_carrito integer NOT NULL,
    id_producto integer NOT NULL,
    cantidad integer NOT NULL
);
    DROP TABLE public.carrito;
       public         heap    postgres    false            �            1259    16750    carrito_id_carrito_seq    SEQUENCE     �   CREATE SEQUENCE public.carrito_id_carrito_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.carrito_id_carrito_seq;
       public          postgres    false    221                       0    0    carrito_id_carrito_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.carrito_id_carrito_seq OWNED BY public.carrito.id_carrito;
          public          postgres    false    220            �            1259    16927    detalle_orden    TABLE     �   CREATE TABLE public.detalle_orden (
    id_detalle integer NOT NULL,
    id_orden integer,
    id_producto integer,
    cantidad integer,
    subtotal double precision
);
 !   DROP TABLE public.detalle_orden;
       public         heap    postgres    false            �            1259    16926    detalle_orden_id_detalle_seq    SEQUENCE     �   CREATE SEQUENCE public.detalle_orden_id_detalle_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 3   DROP SEQUENCE public.detalle_orden_id_detalle_seq;
       public          postgres    false    226                       0    0    detalle_orden_id_detalle_seq    SEQUENCE OWNED BY     ]   ALTER SEQUENCE public.detalle_orden_id_detalle_seq OWNED BY public.detalle_orden.id_detalle;
          public          postgres    false    225            �            1259    16679    orden    TABLE     �   CREATE TABLE public.orden (
    id_orden integer NOT NULL,
    fecha timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    total numeric(10,2) NOT NULL
);
    DROP TABLE public.orden;
       public         heap    postgres    false            �            1259    16678    orden_id_orden_seq    SEQUENCE     �   CREATE SEQUENCE public.orden_id_orden_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.orden_id_orden_seq;
       public          postgres    false    219                       0    0    orden_id_orden_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.orden_id_orden_seq OWNED BY public.orden.id_orden;
          public          postgres    false    218            �            1259    16672    producto    TABLE     �   CREATE TABLE public.producto (
    id_producto integer NOT NULL,
    nombre character varying(100) NOT NULL,
    precio numeric(10,2) NOT NULL
);
    DROP TABLE public.producto;
       public         heap    postgres    false            �            1259    16671    producto_id_producto_seq    SEQUENCE     �   CREATE SEQUENCE public.producto_id_producto_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.producto_id_producto_seq;
       public          postgres    false    217                       0    0    producto_id_producto_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.producto_id_producto_seq OWNED BY public.producto.id_producto;
          public          postgres    false    216            �            1259    16828    productos_orden    TABLE     �   CREATE TABLE public.productos_orden (
    id_producto integer NOT NULL,
    id_orden integer NOT NULL,
    cantidad integer,
    precio_unitario numeric(10,2)
);
 #   DROP TABLE public.productos_orden;
       public         heap    postgres    false            �            1259    16763    usuario    TABLE     �  CREATE TABLE public.usuario (
    id_usuario integer NOT NULL,
    nombre character varying(50) NOT NULL,
    apellido character varying(50) NOT NULL,
    correo character varying(100) NOT NULL,
    tipo character varying(10) NOT NULL,
    "contraseña" character varying(255) NOT NULL,
    nombre_usuario character varying(50) NOT NULL,
    CONSTRAINT usuario_tipo_check CHECK (((tipo)::text = ANY ((ARRAY['admin'::character varying, 'cajero'::character varying])::text[])))
);
    DROP TABLE public.usuario;
       public         heap    postgres    false            �            1259    16762    usuario_id_usuario_seq    SEQUENCE     �   CREATE SEQUENCE public.usuario_id_usuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.usuario_id_usuario_seq;
       public          postgres    false    223                       0    0    usuario_id_usuario_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.usuario_id_usuario_seq OWNED BY public.usuario.id_usuario;
          public          postgres    false    222            Z           2604    16754    carrito id_carrito    DEFAULT     x   ALTER TABLE ONLY public.carrito ALTER COLUMN id_carrito SET DEFAULT nextval('public.carrito_id_carrito_seq'::regclass);
 A   ALTER TABLE public.carrito ALTER COLUMN id_carrito DROP DEFAULT;
       public          postgres    false    221    220    221            \           2604    16930    detalle_orden id_detalle    DEFAULT     �   ALTER TABLE ONLY public.detalle_orden ALTER COLUMN id_detalle SET DEFAULT nextval('public.detalle_orden_id_detalle_seq'::regclass);
 G   ALTER TABLE public.detalle_orden ALTER COLUMN id_detalle DROP DEFAULT;
       public          postgres    false    225    226    226            X           2604    16682    orden id_orden    DEFAULT     p   ALTER TABLE ONLY public.orden ALTER COLUMN id_orden SET DEFAULT nextval('public.orden_id_orden_seq'::regclass);
 =   ALTER TABLE public.orden ALTER COLUMN id_orden DROP DEFAULT;
       public          postgres    false    218    219    219            W           2604    16675    producto id_producto    DEFAULT     |   ALTER TABLE ONLY public.producto ALTER COLUMN id_producto SET DEFAULT nextval('public.producto_id_producto_seq'::regclass);
 C   ALTER TABLE public.producto ALTER COLUMN id_producto DROP DEFAULT;
       public          postgres    false    217    216    217            [           2604    16766    usuario id_usuario    DEFAULT     x   ALTER TABLE ONLY public.usuario ALTER COLUMN id_usuario SET DEFAULT nextval('public.usuario_id_usuario_seq'::regclass);
 A   ALTER TABLE public.usuario ALTER COLUMN id_usuario DROP DEFAULT;
       public          postgres    false    222    223    223                      0    16751    carrito 
   TABLE DATA           D   COPY public.carrito (id_carrito, id_producto, cantidad) FROM stdin;
    public          postgres    false    221   <                 0    16927    detalle_orden 
   TABLE DATA           ^   COPY public.detalle_orden (id_detalle, id_orden, id_producto, cantidad, subtotal) FROM stdin;
    public          postgres    false    226   "<                 0    16679    orden 
   TABLE DATA           7   COPY public.orden (id_orden, fecha, total) FROM stdin;
    public          postgres    false    219   �<                 0    16672    producto 
   TABLE DATA           ?   COPY public.producto (id_producto, nombre, precio) FROM stdin;
    public          postgres    false    217   |=       
          0    16828    productos_orden 
   TABLE DATA           [   COPY public.productos_orden (id_producto, id_orden, cantidad, precio_unitario) FROM stdin;
    public          postgres    false    224   !@       	          0    16763    usuario 
   TABLE DATA           l   COPY public.usuario (id_usuario, nombre, apellido, correo, tipo, "contraseña", nombre_usuario) FROM stdin;
    public          postgres    false    223   >@                  0    0    carrito_id_carrito_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.carrito_id_carrito_seq', 1, false);
          public          postgres    false    220                       0    0    detalle_orden_id_detalle_seq    SEQUENCE SET     J   SELECT pg_catalog.setval('public.detalle_orden_id_detalle_seq', 9, true);
          public          postgres    false    225                       0    0    orden_id_orden_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.orden_id_orden_seq', 20, true);
          public          postgres    false    218                       0    0    producto_id_producto_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.producto_id_producto_seq', 60, true);
          public          postgres    false    216                       0    0    usuario_id_usuario_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.usuario_id_usuario_seq', 11, true);
          public          postgres    false    222            c           2606    16756    carrito carrito_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.carrito
    ADD CONSTRAINT carrito_pkey PRIMARY KEY (id_carrito);
 >   ALTER TABLE ONLY public.carrito DROP CONSTRAINT carrito_pkey;
       public            postgres    false    221            m           2606    16932     detalle_orden detalle_orden_pkey 
   CONSTRAINT     f   ALTER TABLE ONLY public.detalle_orden
    ADD CONSTRAINT detalle_orden_pkey PRIMARY KEY (id_detalle);
 J   ALTER TABLE ONLY public.detalle_orden DROP CONSTRAINT detalle_orden_pkey;
       public            postgres    false    226            a           2606    16685    orden orden_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.orden
    ADD CONSTRAINT orden_pkey PRIMARY KEY (id_orden);
 :   ALTER TABLE ONLY public.orden DROP CONSTRAINT orden_pkey;
       public            postgres    false    219            _           2606    16677    producto producto_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.producto
    ADD CONSTRAINT producto_pkey PRIMARY KEY (id_producto);
 @   ALTER TABLE ONLY public.producto DROP CONSTRAINT producto_pkey;
       public            postgres    false    217            k           2606    16832 $   productos_orden productos_orden_pkey 
   CONSTRAINT     u   ALTER TABLE ONLY public.productos_orden
    ADD CONSTRAINT productos_orden_pkey PRIMARY KEY (id_producto, id_orden);
 N   ALTER TABLE ONLY public.productos_orden DROP CONSTRAINT productos_orden_pkey;
       public            postgres    false    224    224            e           2606    16774    usuario usuario_correo_key 
   CONSTRAINT     W   ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_correo_key UNIQUE (correo);
 D   ALTER TABLE ONLY public.usuario DROP CONSTRAINT usuario_correo_key;
       public            postgres    false    223            g           2606    16776 "   usuario usuario_nombre_usuario_key 
   CONSTRAINT     g   ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_nombre_usuario_key UNIQUE (nombre_usuario);
 L   ALTER TABLE ONLY public.usuario DROP CONSTRAINT usuario_nombre_usuario_key;
       public            postgres    false    223            i           2606    16772    usuario usuario_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);
 >   ALTER TABLE ONLY public.usuario DROP CONSTRAINT usuario_pkey;
       public            postgres    false    223            n           2606    16757     carrito carrito_id_producto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.carrito
    ADD CONSTRAINT carrito_id_producto_fkey FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);
 J   ALTER TABLE ONLY public.carrito DROP CONSTRAINT carrito_id_producto_fkey;
       public          postgres    false    221    217    4703            q           2606    16933 )   detalle_orden detalle_orden_id_orden_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.detalle_orden
    ADD CONSTRAINT detalle_orden_id_orden_fkey FOREIGN KEY (id_orden) REFERENCES public.orden(id_orden);
 S   ALTER TABLE ONLY public.detalle_orden DROP CONSTRAINT detalle_orden_id_orden_fkey;
       public          postgres    false    219    226    4705            r           2606    16938 ,   detalle_orden detalle_orden_id_producto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.detalle_orden
    ADD CONSTRAINT detalle_orden_id_producto_fkey FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);
 V   ALTER TABLE ONLY public.detalle_orden DROP CONSTRAINT detalle_orden_id_producto_fkey;
       public          postgres    false    4703    217    226            o           2606    16833 -   productos_orden productos_orden_id_orden_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.productos_orden
    ADD CONSTRAINT productos_orden_id_orden_fkey FOREIGN KEY (id_orden) REFERENCES public.orden(id_orden);
 W   ALTER TABLE ONLY public.productos_orden DROP CONSTRAINT productos_orden_id_orden_fkey;
       public          postgres    false    4705    219    224            p           2606    16838 0   productos_orden productos_orden_id_producto_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.productos_orden
    ADD CONSTRAINT productos_orden_id_producto_fkey FOREIGN KEY (id_producto) REFERENCES public.producto(id_producto);
 Z   ALTER TABLE ONLY public.productos_orden DROP CONSTRAINT productos_orden_id_producto_fkey;
       public          postgres    false    4703    217    224                  x������ � �         O   x�5˱� C�Z&'a��?G����o8aBb�n�4��S��8�Q7������X�鰪w�>V"�
K<��|�;         �   x�]�ˑ� �3�bXJ�� �2�Ǳ�{vEq�!Z*�		~Xr��CЎ���@ԉ)F)ĺ���<:�V#�y� �T3vS��X5+0{>���x1�]G�S�:��<�v��/�/��0Pg�Bh���"�P9�\��SI*���Yw$�h��s�6��?� �\2?Ɗ�Y�z澑W�|��2@Ш�C��#U�q�Y�y��<X�P��c��dZ�y}$*�=��pn�4�~�/�~x         �  x�e��R�0���S��X7_��B3�����h\3���q�K^�ۼX�u�8jVI����K����q�Ɍ�T��}l6�x����JD��}lMR��Ƶ�Q���-������J�t�6�K֓�mWg'��٤�e�[�.,����f�����x*��g.���vzA1�c<�_M�-�sv=������㻩�32Z1?�Ψ7�1��Mw�9�M��i:�ψ�S~�d�>����Sj~�z��C�LR���A��7<�X��2�`4�^�HO�|�`϶5^�ଲ�T�YBv�c�	�%��� ���՘~l�t�
���g��_;�'H��iњ�4g_��&[�|��'lE�8�k��f_J�@����ڴ����D9�����YR0�䞬�4G����7��`l)=��`vX�e�e��L�e�����EK�Y�ʼ��*���\���4�2��Xt4�ƕ��2Lk�ֆ�
o��mm^�`a�X�M�8&�[2���x�3�NJ��YZA�:�?6%�gF�.)%ǋhz�TȲp���:���t�.����^�'�/���cė��k� 4-�ג:��i1ZE����Ώ�u�n���H�u�0����څ5�<J���Ȓ�َX�_A],A������g.���-��aؿw&��y�Z���d�A      
      x������ � �      	   y   x���1�0��DʅJ���\������o�c �B�!�6�;�������d1��g�kһt.ZE�y8���	<$/be�_a:���I�e-p�j��?�5�脒Vo�}5�Ә授�RfF�     