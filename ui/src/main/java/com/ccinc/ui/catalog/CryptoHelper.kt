package com.ccinc.ui.catalog

import android.util.Base64
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.util.Arrays
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

object CryptoHelper {

    private const val INTERACTION_COUNT = 10000
    private const val KEY_LENGTH = 128
    private const val SALT_LEN = KEY_LENGTH / 8 // same size as key output

    @Throws(Exception::class)
    fun encrypt(pin: String, plaintext: String): String {
        // Generate salt for key generation
        val random = SecureRandom()
        val salt = ByteArray(SALT_LEN)

        random.nextBytes(salt)

        // Generate key from pin
        val key = getRawKey(pin, salt)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

        // Generate IV vector
        val iv = ByteArray(cipher.blockSize)
        random.nextBytes(iv)

        val ivParams = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams)

        val encrypted = cipher.doFinal(plaintext.toByteArray(charset("UTF-8")))
        val c = ByteArray(salt.size + iv.size + encrypted.size)

        System.arraycopy(salt, 0, c, 0, salt.size)
        System.arraycopy(iv, 0, c, salt.size, iv.size)
        System.arraycopy(encrypted, 0, c, salt.size + iv.size, encrypted.size)

        return Base64.encodeToString(c, Base64.NO_WRAP)
    }

    @Throws(Exception::class)
    fun encrypt(token: String): String {
        // Generate salt for key generation
        val random = SecureRandom()
        val salt = ByteArray(SALT_LEN)
        random.nextBytes(salt)

        // Generate key from pin if pin is provided
        val key = getRawKey(generateKeyFromData("123456"), salt) // Use empty pin


        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

        // Generate IV vector
        val iv = ByteArray(cipher.blockSize)
        random.nextBytes(iv)
        val ivParams = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams)

        val encrypted = cipher.doFinal(token.toByteArray(Charsets.UTF_8))
        val c = ByteArray(salt.size + iv.size + encrypted.size)
        System.arraycopy(salt, 0, c, 0, salt.size)
        System.arraycopy(iv, 0, c, salt.size, iv.size)
        System.arraycopy(encrypted, 0, c, salt.size + iv.size, encrypted.size)

        return Base64.encodeToString(c, Base64.NO_WRAP)
    }

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        InvalidKeySpecException::class
    )
    fun decrypt(encrypted: String?): String? {
        val data = Base64.decode(encrypted, Base64.NO_WRAP)
        val salt = Arrays.copyOfRange(data, 0, SALT_LEN)
        val iv = Arrays.copyOfRange(data, SALT_LEN, SALT_LEN + 16)
        val enc = Arrays.copyOfRange(data, SALT_LEN + 16, data.size)
        val key = getRawKey(generateKeyFromData("123456"), salt)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivParameterSpec = IvParameterSpec(iv)

        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec)

        return String(cipher.doFinal(enc))
    }

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeyException::class,
        InvalidKeySpecException::class
    )
    fun decrypt(encrypted: String?, pin: String): String? {
        val data = Base64.decode(encrypted, Base64.NO_WRAP)
        val salt = Arrays.copyOfRange(data, 0, SALT_LEN)
        val iv = Arrays.copyOfRange(data, SALT_LEN, SALT_LEN + 16)
        val enc = Arrays.copyOfRange(data, SALT_LEN + 16, data.size)
        val key = getRawKey(pin, salt)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivParameterSpec = IvParameterSpec(iv)

        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec)

        return String(cipher.doFinal(enc))
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    private fun getRawKey(pass: String, salt: ByteArray): SecretKey {

        val keySpec: KeySpec = PBEKeySpec(
            pass.toCharArray(), salt, INTERACTION_COUNT, KEY_LENGTH
        )
        val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        val keyBytes = keyFactory.generateSecret(keySpec).encoded

        return SecretKeySpec(keyBytes, "AES")
    }

    fun generateKeyFromData(data: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        println("PinEncode -> " + String(digest.digest(data.toByteArray(Charsets.UTF_8))))
        return String(digest.digest(data.toByteArray(Charsets.UTF_8)))
    }
}