# Module 2 CI/CD DevOps

## Tautan Penting

- Production App (Heroku): https://a-rafa-rally-soelistiono-modul-81845caf2692.herokuapp.com
- Product List Page: https://a-rafa-rally-soelistiono-modul-81845caf2692.herokuapp.com/product/list
- Workflow CI: .github/workflows/ci.yml
- Workflow CD: .github/workflows/cd.yml
- Workflow PMD: .github/workflows/pmd.yml

## Refleksi Implementasi CI/CD

### 1. Isu code quality yang diperbaiki dan strategi perbaikannya

Beberapa isu yang saya perbaiki selama exercise:

1. Masalah awa karena runtime Java di Heroku tidak mengikuti toolchain Gradle (Java 21). Saya melakukannya dengan menambahkan `system.properties` dengan `java.runtime.version=21` agar environment build dan runtime konsisten.

2. Muncul error `no main manifest attribute` pada `-plain.jar`. Saya melakukannya dengan memastikan proses deploy menjalankan `bootJar`, termasuk penyesuaian `Procfile` dan konfigurasi Gradle agar artifact yang dipakai benar.

3.Di local masih bisa melakukan running web, tetapi di Heroku menyebabkan view tidak ditemukan atau perilaku anomali yang membuat web error/no content. Saya melakukannya dengan menyamakan return view pada controller dengan nama file template secara konsisten, lalu memvalidasi lewat unit test controller.

### 2. Apakah implementasi saat ini sudah memenuhi definisi CI dan CD?

Menurut saya, implementasi saat ini sudah memenuhi praktik dasar Continuous Integration karena setiap perubahan memicu workflow otomatis untuk menjalankan test (`ci.yml`) dan static analysis (`pmd.yml`). Dengan begitu, integrasi perubahan tidak menunggu validasi manual dan kode selalu dipantau secara rutin. Selain itu, trigger pada `push` dan `pull_request` membantu mendeteksi anomali lebih cepat sebelum perubahan digabungkan ke branch utama.

Untuk Continuous Deployment, saya melakukan implementasi dengan `Push-based approach` karena setiap push ke branch `main` memicu workflow `cd.yml` yang mana akan melakukan build, test, deploy ke Heroku, lalu melakukan verifikasi aplikasi setelah release.