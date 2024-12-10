package com.example.apporder.models

data class User(
    var uid: String? = null,    // ID người dùng (Firebase UID)
    var email: String? = null,  // Email người dùng
    var role: String? = null    // Vai trò (admin/user)
) {
    // Hàm dựng mặc định không tham số, bắt buộc để Firebase khởi tạo
    constructor() : this(null, null, null)
}
