package home.howework.chat

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@RequiresApi(Build.VERSION_CODES.O)
fun generatePermanentSecretKey(): SecretKey {
    val decodedKey: ByteArray = Base64.getDecoder().decode("090102030805060708090A0B0C0D0E0F")
    return SecretKeySpec(decodedKey, "AES")
}

@RequiresApi(Build.VERSION_CODES.O)
fun secretKeyToStringFashionKey(secretKey: SecretKey): String {
    return Base64.getEncoder().encodeToString(secretKey.encoded)
}

@RequiresApi(Build.VERSION_CODES.O)
fun stringFashionKeyToSecretKey(secretKeyFashion: String): SecretKey {
    val decodedKey: ByteArray = Base64.getDecoder().decode(secretKeyFashion)
    return SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
}

fun generateAESKey(): SecretKey {
    val keyGen = KeyGenerator.getInstance("AES")
    keyGen.init(256) // Key size: 128, 192, or 256 bits)
    return keyGen.generateKey()
}

@RequiresApi(Build.VERSION_CODES.O)
fun encryptAES(plainText: String, secretKey: SecretKey): String {
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val encryptedBytes = cipher.doFinal(plainText.toByteArray())
    return Base64.getEncoder().encodeToString(encryptedBytes)
}

@RequiresApi(Build.VERSION_CODES.O)
fun decryptAES(cipherText: String, secretKey: SecretKey): String {
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText))
    return String(decryptedBytes)
}