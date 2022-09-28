package com.example.com.example

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit
import kotlin.math.log2
import kotlin.math.sin
import kotlin.math.tan

@Fork(1)
@Warmup(iterations = 1, time = 3)
@Measurement(iterations = 2, time = 3)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
open class TestClass {

    @Benchmark
    fun testIntArray(): IntArray {
        return intArrayOf(0, 0, 0)
    }

    @Benchmark
    fun testTypedArray(): IntArray {
        return IntArray(3)
    }
}