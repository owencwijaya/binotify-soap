# BiNotify SOAP
> _SOAP server_ untuk manajemen data _subscription_
> Implementasi untuk Tugas Besar 2 IF3110 Pengembangan Berbasis Web, Prodi Informatika ITB Tahun Ajaran 2022/2023

## Deskripsi Singkat
_SOAP server BiNotify_ digunakan untuk mengelola dan meng-_update_ data _subscription_ dari masing-masing pengguna pada <a href = "https://gitlab.informatika.org/if3110-2022-k01-02-11/binotify-app">BiNotify App</a> yang ingin menggunakan fitur _Premium_ pada aplikasi tersebut. _SOAP server_ akan berinteraksi dengan <a href = "https://gitlab.informatika.org/if3110-2022-k01-02-11/binotify-rest">BiNotify REST (REST server)</a> untuk melakukan pengubahan status _subscription_ dan BiNotify App untuk menerima dan membuatkan _subscription_. _SOAP server_ ini dibangun menggunakan bahasa pemrograman Java dan _library_ _JakartaXML_ untuk bertukar pesan via _XML_. _Server_ ini juga akan melakukan _callback_ ke _database_ duplikat pada BiNotify App apabila ada perubahan data, serta digunakan oleh BiNotify App untuk _polling_ status permintaan _subscription_

## Skema Basis Data
Skema basis data yang digunakan adalah sebagai berikut:
```
subscription
├── creator_id (string), ID artis dari BiNotify Rest yang ingin di-subscribe pengguna 
├── subscriber_id (int), ID pengguna yang hendak melakukan subscription dari BiNotify App
└── status (ENUM('PENDING', 'ACCEPTED', 'REJECTED')), menandakan status subscription

logging
├── id (int), identifier untuk sebuah log
├── description (string), berisi deskripsi gabungan header dan method sebuah request
├── ip_address (string), berisi IP address dari pengirim
├── endpoint (string), berisi endpoint yang dituju
└── requested_at (timestamp), berisi waktu penerimaan request di server
```

## Daftar _Requirements_
Perangkat lunak yang dibutuhkan untuk bisa mengoperasikan BiNotify adalah:
- Docker versi 20.10.21, <a href = "https://docs.docker.com/engine/install/">*panduan instalasi* </a>
- Docker Compose versi 1.26.2, <a href = "https://docs.docker.com/compose/install/">*panduan instalasi* </a>
- _Operating system_ berbasis _Windows 10_ atau _Linux Ubuntu 20.04_
- _Java Development Kit_ yang telah dikonfigurasi beserta _build tool Maven_ , <a href = "https://docs.oracle.com/cd/E19182-01/820-7851/inst_cli_jdk_javahome_t/">*panduan instalasi*</a>

Pengguna juga harus melakukan _setup_ terkait repo berikut:
1. <a href = "https://gitlab.informatika.org/if3110-2022-k01-02-11/binotify-config">BiNotify Config</a>, untuk mengakses database utama (**wajib** untuk penggunaan umum)
2. <a href = "https://gitlab.informatika.org/if3110-2022-k01-02-11/binotify-app">BiNotify App</a>, untuk melakuukan demonstrasi _callback_ (**opsional**)
## Cara Menjalankan
1. **[IMPORTANT]** Pastikan Java dan Maven sudah ter-install dengan benar, dan _repository_ ini sudah di-_clone_
2. Pada _folder root_ , lakukan instalasi _dependency_ dengan perintah berikut:
```
mvn dependency:resolve
```
3. Lakukan kompilasi _source code_ dan jalankan _Docker_ dengan perintah berikut
```
./mvnw clean compile install
docker-compose down && docker-compose build && docker-compose up
```
4. Apabila terkonfigurasi dengan benar, daftar WSDL dapat diakses pada http://localhost:4000/binotify-soap/services/subscription/getSubs

## Pembagian Tugas
Legenda NIM adalah sebagai berikut:
- 13520043: Muhammad Risqi Firdaus
- 13520117: Hafidz Nur Rahman Ghozali
- 13520124: Owen Christian Wijaya

Daftar Pengerjaan
- **Konfigurasi Server dan Database**: 13520043
- **Request Daftar Subscription**: 13520043, 13520117, 13520124
- **Request Membuat Subscription**: 13520124
- **Request Mengubah Status Subscription**: 13520124
- **Endpoint untuk Verifikasi Subscription Pengguna**: 13520043, 13520124
- **Endpoint Polling untuk Cek Status Penggunaan**: 13520117, 13520124