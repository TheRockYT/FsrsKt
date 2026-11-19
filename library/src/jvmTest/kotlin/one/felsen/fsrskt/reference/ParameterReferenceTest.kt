package one.felsen.fsrskt.reference

import one.felsen.fsrskt.fsrs6.FsrsParameters
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Compares [FsrsParameters] against the parameters of py-fsrs.
 */
class ParameterReferenceTest {

    @Test
    fun defaultParametersMatchPyFsrs() {
        assertEquals(ReferenceFixtures.parameterSets.getValue("default"), FsrsParameters.defaultParameters)
    }
}
