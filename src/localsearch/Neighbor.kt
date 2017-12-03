package localsearch

data class Neighbor(val deltaFn: () -> Int, val applyFn: () -> Unit) {

    var invoked = false

    val delta: Int by lazy { deltaFn() }

    inline operator fun invoke() {
        assert(!invoked)
        invoked = true
        applyFn()
    }

}