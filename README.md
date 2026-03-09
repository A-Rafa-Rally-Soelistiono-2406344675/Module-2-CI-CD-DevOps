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

---
# Module 3 OO Principles & Software Maintainability
## Refleksi Implementasi SOLID

### 1) Prinsip apa yang diterapkan pada project ini?
----
### SRP (Single Responsibility Principle)

`ProductController` sekarang hanya menangani alur fitur produk, sedangkan `CarController` dipisah ke file sendiri untuk menangani alur fitur mobil. Dengan pemisahan ini, masing-masing class punya satu alasan perubahan: perubahan fitur produk tidak memaksa perubahan class mobil, dan sebaliknya.

### OCP (Open/Closed Principle)

Service (`ProductServiceImpl` dan `CarServiceImpl`) kini bekerja lewat abstraction repository (`ProductReadRepository`, `ProductWriteRepository`, `CarReadRepository`, `CarWriteRepository`). Jika nanti ingin menambah implementasi baru, misalnya database SQL/NoSQL, kita cukup membuat class repository baru yang mengimplementasikan interface yang sama tanpa memodifikasi logic service. Artinya module terbuka untuk extension, tetapi tertutup untuk modifikasi yang tidak perlu.

### LSP (Liskov Substitution Principle)

`CarController` mewarisi `ProductController`, padahal secara perilaku dan domain tidak substitutable. Sekarang inheritance tersebut dihapus, sehingga tidak ada lagi relasi turunan yang melanggar ekspektasi base class. Dengan ini, struktur class tidak lagi memaksakan substitusi yang salah.

### ISP (Interface Segregation Principle)

Kontrak repository dipisah menjadi interface kecil dan spesifik, yaitu read interface (`ProductReadRepository`, `CarReadRepository`) dan write interface (`ProductWriteRepository`, `CarWriteRepository`). Dengan pendekatan ini, client tidak dipaksa bergantung pada method yang tidak relevan sehingga interface menjadi lebih fokus dan mudah dipelihara.

### DIP (Dependency Inversion Principle)

Controller bergantung ke abstraction service (`ProductService`, `CarService`), bukan implementasi konkret, dan service bergantung ke abstraction repository (read/write interfaces), bukan class repository konkret. Dependency injection juga menggunakan constructor injection agar dependensi eksplisit dan mudah diuji. Dengan demikian high-level module tidak tergantung detail low-level; keduanya bergantung pada abstraction.

### 2) Apa keuntungan menerapkan SOLID pada project ini? (dengan contoh)

Keuntungan utama menerapkan SOLID pada project ini:

1. **Mudah dikembangkan (extensible)**  
   saat ingin mengganti penyimpanan dari in-memory ke database SQL, kita bisa membuat implementasi repository baru berdasarkan interface yang sama tanpa mengubah service.

2. **Lebih mudah diuji (testable)**  
   di `ProductServiceImplTest` dan `CarControllerTest`, dependency dapat di-mock lewat interface sehingga unit test lebih sederhana dan stabil.

3. **Perubahan lebih aman (maintainable)**  
   perubahan fitur car tidak lagi memengaruhi `ProductController` karena controller sudah dipisah berdasarkan tanggung jawab.

4. **Struktur kode lebih jelas**  
   pemisahan read/write repository membuat alur dependency lebih mudah dipahami saat debugging maupun code review.

### 3) Apa kerugian jika SOLID tidak diterapkan pada project ini? (dengan contoh)

1. **Tanggung jawab class bercampur**  
   jika logic product dan car berada di satu controller, perubahan kecil pada fitur car bisa menimbulkan regresi di fitur product.

2. **Coupling tinggi**  
   jika controller langsung bergantung pada `CarServiceImpl`, mengganti implementasi service akan memaksa perubahan di banyak class.

3. **Desain inheritance tidak tepat**  
   `CarController extends ProductController` membuat relasi "is-a" yang tidak valid sehingga mudah melanggar konsistensi perilaku.

4. **Interface terlalu besar**  
   tanpa ISP, client dipaksa mengetahui method yang tidak digunakan, yang memperbesar risiko perubahan berdampak ke modul lain.

---
# Module 4 Test-Driven Development & Refactoring
## Refleksi Implementasi TDD 

### 1) Reflect based on Percival (2017) proposed self-reflective questions (in “Principles and Best Practice of Testing” submodule, chapter “Evaluating Your Testing Objectives”), whether this TDD flow is useful enough for you or not. If not, explain things that you need to do next time you make more tests.

Berdasarkan pertanyaan reflektif dari Percival (2017) dalam bagian Evaluating Your Testing Objectives, menurut saya alur Test-Driven Development yang digunakan pada latihan ini cukup membantu dalam proses pengembangan perangkat lunak. Seperti contoh kasus, saya menulis unit test terlebih dahulu sebelum mengimplementasikan kode program yang akan dibangun. Hal ini membantu saya memahami kebutuhan sistem dengan lebih jelas serta memastikan bahwa setiap fitur yang dibuat benar-benar memenuhi requirement yang telah ditentukan. Selain itu, TDD juga membantu dalam menjaga kualitas kode karena setiap perubahan yang dilakukan dapat langsung diuji melalui test yang sudah dibuat sebelumnya. Ketika sebuah perubahan menyebabkan test gagal, saya dapat segera mengetahui bagian mana yang perlu diperbaiki.

Namun, pada awalnya saya merasa proses TDD terasa lebih lambat karena harus menulis test terlebih dahulu sebelum membuat implementasi kode. Pada akhirnya, saya berpikir bahwa teknik/metode ini dianggap penting di masa depan agar lebih memperhatikan skenario edge case agar test yang dibuat dapat mencakup lebih banyak kemungkinan kondisi yang terjadi pada sistem.

### 2) You have created unit tests in Tutorial. Now reflect whether your tests have successfully followed F.I.R.S.T. principle or not. If not, explain things that you need to do the next time you create more tests.

Dalam tutorial ini, unit test yang dibuat sebagian besar telah mengikuti prinsip F.I.R.S.T, yaitu:

1. **Fast**
   Test yang dibuat berjalan dengan cepat karena hanya menguji unit kecil dari program dan tidak bergantung pada database.

2. **Independent**
   Setiap test dijalankan secara independen dan tidak bergantung pada test lainnya. Hal ini memastikan bahwa kegagalan satu test tidak mempengaruhi hasil test yang lain.

3. **Repeatable**
   Test dapat dijalankan berulang kali dengan hasil yang karena tidak bergantung pada kondisi lingkungan tertentu.

4. **Self-validating**
   Setiap test memiliki assertion yang secara otomatis menentukan apakah test berhasil atau gagal tanpa perlu pengecekan manual.

5. **Timely**
   Test dibuat sebelum implementasi kode, sehingga membantu memastikan bahwa kode yang ditulis benar-benar memenuhi kebutuhan test tersebut.


