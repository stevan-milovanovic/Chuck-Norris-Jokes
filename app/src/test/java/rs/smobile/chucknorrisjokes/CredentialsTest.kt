package rs.smobile.chucknorrisjokes

import org.junit.Assert
import org.junit.Test

class CredentialsTest {

    @Test
    fun testCredentialsLength() {
        Assert.assertEquals(50, BuildConfig.API_KEY_HEADER_VALUE.length)
        Assert.assertEquals(36, BuildConfig.APP_CENTER_API_KEY.length)
    }

}