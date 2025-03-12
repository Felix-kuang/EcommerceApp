# Dokumentasi API - Spring Boot eCommerce

---

## 1. **AuthController**

### Endpoint: `/api/v1/auth/register`
- **Method**: `POST`
- **Deskripsi**: Registrasi pengguna baru.
- **Request Body**: `AuthRequestDTO` (Validasi diperlukan)
- **Response**: HTTP status 200, berisi data pengguna yang berhasil didaftarkan.

### Endpoint: `/api/v1/auth/login`
- **Method**: `POST`
- **Deskripsi**: Melakukan login dan mendapatkan token JWT.
- **Request Body**: `LoginRequestDTO` (Validasi diperlukan)
- **Response**: 
    - HTTP status 200, berisi token JWT.
    - HTTP status 401 jika password tidak valid.

---

## 2. **CartController**

### Endpoint: `/api/v1/cart/add`
- **Method**: `POST`
- **Deskripsi**: Menambahkan produk ke dalam keranjang belanja.
- **PreAuthorize**: `USER` atau `ADMIN`
- **Request Body**: `UpdateCartItemDTO` (Validasi diperlukan)
- **Response**: HTTP status 200, berisi objek `CartDTO` setelah produk ditambahkan.

### Endpoint: `/api/v1/cart`
- **Method**: `GET`
- **Deskripsi**: Mengambil isi keranjang belanja berdasarkan username.
- **PreAuthorize**: `USER` atau `ADMIN`
- **Request Body**: `String username`
- **Response**: HTTP status 200, berisi objek `CartDTO`.

### Endpoint: `/api/v1/cart/update`
- **Method**: `PUT`
- **Deskripsi**: Memperbarui item dalam keranjang belanja.
- **PreAuthorize**: `USER` atau `ADMIN`
- **Request Body**: `UpdateCartItemDTO` (Validasi diperlukan)
- **Response**: HTTP status 200, pesan sukses.

### Endpoint: `/api/v1/cart/remove`
- **Method**: `DELETE`
- **Deskripsi**: Menghapus item dari keranjang belanja.
- **PreAuthorize**: `USER` atau `ADMIN`
- **Request Body**: `RemoveFromCartDTO` (Validasi diperlukan)
- **Response**: HTTP status 200, pesan sukses.

---

## 3. **OrderController**

### Endpoint: `/api/v1/orders/checkout`
- **Method**: `POST`
- **Deskripsi**: Melakukan checkout untuk user yang sedang login.
- **PreAuthorize**: `USER` atau `ADMIN`
- **Request Body**: `String username`
- **Response**: HTTP status 200, berisi objek `OrderDTO` jika checkout berhasil.
- **Response**: HTTP status 403 jika user yang melakukan checkout tidak sesuai dengan yang login.

---

## 4. **PaymentController**

### Endpoint: `/api/v1/payments/process`
- **Method**: `POST`
- **Deskripsi**: Memproses pembayaran untuk pesanan.
- **PreAuthorize**: `USER` atau `ADMIN`
- **Request Body**: `PaymentDTO` (Validasi diperlukan)
- **Response**: 
    - HTTP status 200 jika pembayaran berhasil diproses.
    - HTTP status 400 jika terjadi error pada proses pembayaran.

### Endpoint: `/api/v1/payments/callback`
- **Method**: `POST`
- **Deskripsi**: Callback untuk menangani pembayaran yang selesai.
- **PreAuthorize**: `USER` atau `ADMIN`
- **Request Body**: `PaymentCallbackDTO` (Validasi diperlukan)
- **Response**: 
    - HTTP status 200 jika callback berhasil diproses.
    - HTTP status 400 jika terjadi error pada proses callback.

---

## 5. **ProductController**

### Endpoint: `/api/v1/products`
- **Method**: `GET`
- **Deskripsi**: Mengambil semua produk yang ada.
- **Response**: HTTP status 200, berisi daftar produk (`List<Product>`).

### Endpoint: `/api/v1/products/{id}`
- **Method**: `GET`
- **Deskripsi**: Mengambil produk berdasarkan ID.
- **Path Variable**: `id` (Long)
- **Response**: 
    - HTTP status 200, berisi objek `Product`.
    - HTTP status 404 jika produk tidak ditemukan.

### Endpoint: `/api/v1/products`
- **Method**: `POST`
- **Deskripsi**: Menambahkan produk baru (hanya untuk admin).
- **PreAuthorize**: `ADMIN`
- **Request Body**: `CreateProductDTO` (Validasi diperlukan)
- **Response**: HTTP status 200, berisi objek `Product` yang baru dibuat.

### Endpoint: `/api/v1/products/{id}`
- **Method**: `PUT`
- **Deskripsi**: Memperbarui produk berdasarkan ID (hanya untuk admin).
- **PreAuthorize**: `ADMIN`
- **Path Variable**: `id` (Long)
- **Request Body**: `UpdateProductDTO` (Validasi diperlukan)
- **Response**: HTTP status 200, berisi objek `Product` yang sudah diperbarui.
- **Response**: HTTP status 404 jika produk tidak ditemukan.

### Endpoint: `/api/v1/products/{id}`
- **Method**: `DELETE`
- **Deskripsi**: Menghapus produk berdasarkan ID (hanya untuk admin).
- **PreAuthorize**: `ADMIN`
- **Path Variable**: `id` (Long)
- **Response**: HTTP status 200, berisi pesan konfirmasi produk telah dihapus.

---
