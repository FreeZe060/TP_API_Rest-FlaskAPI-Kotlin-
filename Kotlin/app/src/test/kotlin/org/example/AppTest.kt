package org.example

import org.junit.jupiter.api.Test  // Import de JUnit 5
import kotlin.test.assertEquals

class AppTest {

    @Test
    fun testGreeting() {
        val greeting = "Hello, World!"  // Exemple de logique à tester
        assertEquals("Hello, World!", greeting)
    }
}
