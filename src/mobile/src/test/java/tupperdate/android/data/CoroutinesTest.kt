package tupperdate.android.data

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CoroutinesTest {

    @Test
    fun testDropAfterInstanceWorks() = runBlocking {
        open class A
        class B : A()

        val list = flowOf(A(), A(), B(), A(), B(), A())
            .dropAfterInstance<A, B>()
            .toList()

        val expected = listOf(A(), A(), B(), B())

        list.forEachIndexed { idx, elem ->
            assertEquals(expected[idx]::class, elem::class)
        }
    }
}
