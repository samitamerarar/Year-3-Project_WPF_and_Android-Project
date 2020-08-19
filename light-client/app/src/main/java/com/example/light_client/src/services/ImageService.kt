package com.example.light_client.src.services

import android.os.Handler
import com.example.light_client.src.core.Constants
import com.example.light_client.src.core.Http


class ImageService {
    data class ImageId(var id: String)
    data class ImageData(var data: String)

    companion object {
        fun upload(image: String, username: String, authService: AuthService, handler: Handler) {
            val url = "${Constants.BASE_URL}/image/upload"
            Http.post<ImageId>(url, ImageData(image)) {
                authService.updateProfile(username, AuthService.ProfileUpdate(null, null, it.data.id), handler)
            }
        }
    }
}
