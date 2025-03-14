create database QLTT;

create table SinhVien(
	id bigint primary key not null,
	masv varchar(10) unique not null,
	tensv nvarchar(30),
	lop varchar(10),
	gioitinh bit,
	diachi nvarchar(100),
	sdt varchar(10),
	namsinh varchar(10)
);

create table SinhVien_SEQ(
	last_value bigint,
	allocation_size int,
	next_value as (last_value + allocation_size)
);

create table MonHoc(
	id bigint primary key not null,
	mamh varchar(10) unique not null,
	tenmh nvarchar(20),
	sotinchi int
);

create table Diem(
	id bigint primary key not null,
	masv varchar(10) not null,
	mamh varchar(10) not null,
	diemcc float,
	dieml1 float,
	dieml2 float,
	dieml3 float,
	dieml4 float,
	diemck float,
	diemtb char(1) check (diemtb in ('A', 'B', 'C', 'D')),
	kihoc int,
	namhoc int,
	foreign key (masv) references SinhVien(masv),
	foreign key (mamh) references MonHoc(mamh),
);

create table Diem_SEQ(
	last_value bigint,
	allocation_size int,
	next_value as (last_value + allocation_size)
);

create table DanhGia(
	id bigint primary key not null,
	masv varchar(10) not null,
	foreign key (masv) references SinhVien(masv),
	kihoc int,
	namhoc int,
	thaidohoctap nvarchar(10) check (thaidohoctap in (N'Tốt', N'Khá', N'Trung bình', N'Yếu')),
	diemrenluyen nvarchar(10) check (diemrenluyen in (N'Tốt', N'Khá', N'Trung bình', N'Yếu')),
	ketquadanhgia nvarchar(10) check (ketquadanhgia in (N'Tốt', N'Khá', N'Trung bình', N'Yếu')),
);

create table DanhGia_SEQ(
	last_value bigint,
	allocation_size int,
	next_value as (last_value + allocation_size)
);


create table KhenThuong(
	id bigint primary key not null,
	masv varchar(10) not null,
	foreign key (masv) references SinhVien(masv),
	diem float,
	danhhieu nvarchar(10) check (danhhieu in (N'Xuất sắc', N'Giỏi')),
	namhoc int,
	tinhtrang bit,
	lido nvarchar(1000)
);

create table KhenThuong_SEQ(
	last_value bigint,
	allocation_size int,
	next_value as (last_value + allocation_size)
);