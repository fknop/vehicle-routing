package localsearch

data class Successor (private val deltaFn: () -> Int, private val applyFn: () -> Unit) {

    private var invoked = false

    val delta: Int by lazy { deltaFn() }

    operator fun invoke() {
        assert(!invoked)
        invoked = true
        applyFn()
    }

}